package cl.camanchaca.orders.adapters.excel;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelDemandOrderAdminAdapter implements ExcelRepository<UnrestrictedDemand> {

    private final UnrestrictedDemandRepository unrestrictedDemandRepository;
    @Override
    public Flux<UnrestrictedDemand> readFile(InputStream file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();
        List<UnrestrictedDemand> unrestrictedDemands = new ArrayList<>();

        if (iterator.hasNext()) {
            iterator.next();
        }

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            UnrestrictedDemand unrestrictedDemand = createUnrestrictedDemandFromRow(currentRow);
            unrestrictedDemands.add(unrestrictedDemand);
        }

        workbook.close();
        return Flux.fromIterable(unrestrictedDemands);
    }

    private UnrestrictedDemand createUnrestrictedDemandFromRow(Row row) {
        UnrestrictedDemand.UnrestrictedDemandBuilder builder = UnrestrictedDemand.builder();

        // Columna 0: especie (String)
        builder.especie(getCellValueAsString(row.getCell(0)));

        // Columna 1: nombreSector (String)
        builder.nombreSector(getCellValueAsString(row.getCell(1)));

        // Columna 2: paisDestino (String)
        builder.paisDestino(getCellValueAsString(row.getCell(2)));

        // Columna 3: destinatario (String)
        builder.destinatario(getCellValueAsString(row.getCell(3)));

        // Columna 4: tipoContrato (String)
        builder.tipoContrato(getCellValueAsString(row.getCell(4)));

        // Columna 5: dv (String)
        builder.dv(getCellValueAsString(row.getCell(5)));

        // Columna 6: contrato (String)
        builder.contrato(getCellValueAsString(row.getCell(6)));

        // Columna 7: calidad (String)
        builder.calidad(getCellValueAsString(row.getCell(7)));

        // Columna 8: puertoDestino (String)
        builder.puertoDestino(getCellValueAsString(row.getCell(8)));

        // Columna 9: codigo (String)
        builder.productsId(getCellValueAsInteger(row.getCell(9)));

        // Columna 10: forma (String)
        builder.forma(getCellValueAsString(row.getCell(10)));

        // Columna 11: calibre (String)
        builder.calibre(getCellValueAsString(row.getCell(11)));

        // Columna 12: familia (String)
        builder.familia(getCellValueAsString(row.getCell(12)));

        // Columna 13: oficina (String)
        builder.oficina(getCellValueAsString(row.getCell(13)));

        // Columna 14: kilosNetos (BigDecimal)
        builder.kilosNetos(getCellValueAsBigDecimal(row.getCell(14)));

        // Columna 15: fob (BigDecimal)
        builder.fob(getCellValueAsBigDecimal(row.getCell(15)));

        // Columna 16: rmp (BigDecimal)
        builder.rmp(getCellValueAsBigDecimal(row.getCell(16)));

        // Columna 17: auxOp (String)
        builder.auxOp(getCellValueAsString(row.getCell(17)));

        // Columna 18: rend (BigDecimal)
        builder.rend(getCellValueAsBigDecimal(row.getCell(18)));

        // Columna 19: kgWFEDemanda (BigDecimal)
        builder.kgWFEDemanda(getCellValueAsBigDecimal(row.getCell(19)));

        // Columna 20: kgWFEPlan (BigDecimal)
        builder.kgWFEPlan(getCellValueAsBigDecimal(row.getCell(20)));

        // Columna 21: dif (String)
        builder.dif(getCellValueAsString(row.getCell(21)));

        // Columna 22: periodo (LocalDate)
        builder.periodo(getCellValueAsLocalDate(row.getCell(22)));

        // Columna 23: precioCierreUSDKgNeto (BigDecimal)
        builder.precioCierreUSDKgNeto(getCellValueAsBigDecimal(row.getCell(23)));

        // Columna 24: flete (BigDecimal)
        builder.flete(getCellValueAsBigDecimal(row.getCell(24)));

        // Columna 25: libras (BigDecimal)
        builder.libras(getCellValueAsBigDecimal(row.getCell(25)));

        // Columna 26: estatus (String)
        builder.estatus(getCellValueAsString(row.getCell(26)));

        // Columna 27: periodoCarga (LocalDate)
        builder.periodoCarga(getCellValueAsLocalDate(row.getCell(27)));

        // Columna 28: orderType (String)
        builder.orderType(getCellValueAsString(row.getCell(28)));

        // Columna 29: customerProgram (String)
        builder.customerProgram(getCellValueAsString(row.getCell(29)));

        // Columna 30: officeSalesType (String)
        builder.officeSalesType(getCellValueAsString(row.getCell(30)));

        // Columna 31: extRmp (String)
        builder.family(getCellValueAsString(row.getCell(31)));

        // Columna 31: extRmp (String)
        builder.extRmp(getCellValueAsString(row.getCell(32)));

        // Columna 32: trimDlbWK (BigDecimal)
        builder.trimDlbWK(getCellValueAsBigDecimal(row.getCell(33)));

        // Columna 33: estado (String)

        // Columna 34: usuario (String)

        return builder.build();
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

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return 0;
        }
        switch (cell.getCellType()) {
            case STRING:
                return Integer.valueOf(cell.getStringCellValue());
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            default:
                return 0;
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
