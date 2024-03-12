package cl.camanchaca.optimization.adapter.excel;

import cl.camanchaca.business.repositories.optimization.BulkExcelRepository;
import cl.camanchaca.business.repositories.optimization.DemandPeriodRepository;
import cl.camanchaca.business.usecases.optimization.GetSelectedPeriodUseCase;
import cl.camanchaca.domain.models.optimization.DemandPeriod;
import cl.camanchaca.domain.models.optimization.Period;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DemandPeriodExcelAdapter implements BulkExcelRepository<DemandPeriod> {

    private final GetSelectedPeriodUseCase periodUseCase;
    private final DemandPeriodRepository demandPeriodRepository;
    @Override
    public Flux<DemandPeriod> readFile(InputStream file, Map<String, String> headers) throws IOException {
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();
        List<DemandPeriod> unrestrictedDemands = new ArrayList<>();

        Row headerRow = sheet.getRow(0);
        List<String> columnNames = getColumnNames(headerRow);

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();

            if(currentRow.getRowNum() == 0) {
                continue;
            }

            DemandPeriod unrestrictedDemand = createDemandPeriod(currentRow, columnNames);
            unrestrictedDemands.add(unrestrictedDemand);
        }

        workbook.close();

        return Flux.fromIterable(unrestrictedDemands)
                .flatMap(demandPeriod ->
                        demandPeriodRepository.getPeriodByPeriodINAndDemandUUID(demandPeriod.getPeriods().stream().map(Period::getPeriod).collect(Collectors.toList()), demandPeriod.getUnrestrictedDemandId())
                                .collectList()
                                .map(updatedPeriods -> {
                                    demandPeriod.setPeriods(updatedPeriods);
                                    return demandPeriod;
                                })
                );
        
    }

    private DemandPeriod createDemandPeriod(Row row, List<String> columnNames) {
        DemandPeriod.DemandPeriodBuilder builder = DemandPeriod.builder();

        builder
                .unrestrictedDemandId(UUID.fromString(getCellValueAsString(row.getCell(0))))
                .codigo(getCellValueAsString(row.getCell(0)))
                .oficina(getCellValueAsString(row.getCell(1)))
                .familia(getCellValueAsString(row.getCell(2)))
                .pais(getCellValueAsString(row.getCell(3)))
                .dv(getCellValueAsString(row.getCell(4)));

        mapSpecialColumns(row, builder, columnNames);

        return builder.build();
    }

    private void mapSpecialColumns(Row row, DemandPeriod.DemandPeriodBuilder builder, List<String> columnNames) {
        List<Period> periodList = new ArrayList<>();

        for (int i = 6; i < columnNames.size(); i++) {
            Cell cell = row.getCell(i);
            // Obtener la fecha correspondiente de la fila de encabezado
            String dateStr = columnNames.get(i);
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (Exception e) {
                continue;
            }

            boolean status = cell != null && cell.getCellType() != CellType.BLANK;

            Period period = Period.builder()
                    .period(date)
                    .status(status)
                    .build();
            periodList.add(period);
        }
        builder.periods(periodList);
    }

    private List<String> getColumnNames(Row headerRow) {
        List<String> columnNames = new ArrayList<>();
        for (Cell cell : headerRow) {
            try{
                columnNames.add(getCellValueAsString(cell));
            }catch (Exception e){
                columnNames.add(getCellValueAsLocalDate(cell).toString());
            }
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
                throw new RuntimeException("No se esperaba un valor numérico en una celda de texto");
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
