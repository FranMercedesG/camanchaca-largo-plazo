package cl.camanchaca.orders.adapters.excel;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.domain.models.demand.*;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class ExcelDemandOrderOfficeAdapter implements ExcelRepository<UnrestrictedDemandOfficeExcel> {
    @Override
    public Flux<UnrestrictedDemandOfficeExcel> readFile(InputStream file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();
        List<UnrestrictedDemandOfficeExcel> unrestrictedDemands = new ArrayList<>();

        Row headerRow = sheet.getRow(1);
        List<String> columnNames = getColumnNames(headerRow);

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();

            if(currentRow.getRowNum() == 0 || currentRow.getRowNum() == 1) {
                continue;
            }

            UnrestrictedDemandOfficeExcel unrestrictedDemand = createUnrestrictedDemandFromRow(currentRow, columnNames);
            unrestrictedDemands.add(unrestrictedDemand);
        }

        workbook.close();
        return Flux.fromIterable(unrestrictedDemands);
    }

    private UnrestrictedDemandOfficeExcel createUnrestrictedDemandFromRow(Row row, List<String> columnNames) {
        UnrestrictedDemandOfficeExcel.UnrestrictedDemandOfficeExcelBuilder builder = UnrestrictedDemandOfficeExcel.builder();

        builder
                .oficina(getCellValueAsString(row.getCell(0)))
                .officeSalesType(getCellValueAsString(row.getCell(1)))
                .customerProgram(getCellValueAsString(row.getCell(2)))
                .paisDestino(getCellValueAsString(row.getCell(3)))
                .estatus(getCellValueAsString(row.getCell(4)))
                .codigo(getCellValueAsString(row.getCell(5)))
                .destinatario(getCellValueAsString(row.getCell(6)))
                .especie(getCellValueAsString(row.getCell(7)))
                .calidad(getCellValueAsString(row.getCell(8)))
                .familia(getCellValueAsString(row.getCell(9)))
                .orderType(getCellValueAsString(row.getCell(10)))
                .forma(getCellValueAsString(row.getCell(11)))
                .calibre(getCellValueAsString(row.getCell(12)))
                .rend(getCellValueAsBigDecimal(row.getCell(13)))
                .puertoDestino(getCellValueAsString(row.getCell(16)));

        mapSpecialColumns(row, builder, columnNames);

        return builder.build();
    }

    private void mapSpecialColumns(Row row, UnrestrictedDemandOfficeExcel.UnrestrictedDemandOfficeExcelBuilder builder, List<String> columnNames) {
        List<FobDetail> fob = new ArrayList<>();
        List<PrecioCierreUsdKgNetoDetail> purchPrice =  new ArrayList<>();
        List<RmpDetail> rmpDetails = new ArrayList<>();
        List<LibrasProdFwDetail> prodFw = new ArrayList<>();
        for (int i = 0; i < columnNames.size(); i++) {
            Row period = row.getSheet().getRow(0);
            String columnName = columnNames.get(i);
            Cell cell = row.getCell(i);

            BigDecimal cellValue = getCellValueAsBigDecimal(cell);

            if (!columnName.contains("Purch Price") && !columnName.contains("Prod FW") && !columnName.contains("RMP") && !columnName.contains("FOB")) {
                continue;
            }

            if (columnName.contains("FOB")) {
                fob.add(FobDetail.builder().value(cellValue).date(getCellValueAsLocalDate(period.getCell(i))).build());
            } else if (columnName.contains("Purch Price")) {
                getCellValueAsBigDecimal(period.getCell(i));
                purchPrice.add(PrecioCierreUsdKgNetoDetail.builder().date(getCellValueAsLocalDate(period.getCell(i))).value(cellValue).build());
            } else if (columnName.contains("RMP")) {
                rmpDetails.add(RmpDetail.builder().date(getCellValueAsLocalDate(period.getCell(i))).value(cellValue).build());
            } else if (columnName.contains("Prod FW")) {
                prodFw.add(LibrasProdFwDetail.builder().date(getCellValueAsLocalDate(period.getCell(i))).value(cellValue).build());
            }
        }
        builder.purchPrice(purchPrice);
        builder.fob(fob);
        builder.rmp(rmpDetails);
        builder.prodFw(prodFw);

    }

    private List<String> getColumnNames(Row headerRow) {
        List<String> columnNames = new ArrayList<>();
        for (Cell cell : headerRow) {
            columnNames.add(cell.getStringCellValue());
        }
        return columnNames;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return BigDecimal.ZERO;
                }
            default:
                return BigDecimal.ZERO;
        }
    }

    private LocalDate getCellValueAsLocalDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                try {
                    return LocalDate.parse(cell.getStringCellValue());
                } catch (Exception e) {
                    return null;
                }
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    try {
                        Date date = cell.getDateCellValue();
                        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    } catch (Exception e) {
                        return null;
                    }
                }
                break;
            default:
                return null;
        }
        return null;
    }


}
