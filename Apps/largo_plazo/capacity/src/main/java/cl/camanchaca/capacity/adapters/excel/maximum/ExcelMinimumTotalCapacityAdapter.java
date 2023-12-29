package cl.camanchaca.capacity.adapters.excel.maximum;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacityValue;
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
public class ExcelMinimumTotalCapacityAdapter implements ExcelRepository<MinimumCapacity> {
    @Override
    public Flux<MinimumCapacity> readFile(InputStream file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();
        List<MinimumCapacity> maximumCapacities = new ArrayList<>();


        if (iterator.hasNext()) {
            iterator.next();
        }

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();

            if (isRowEmpty(currentRow)) {
                break;
            }

            MinimumCapacity capacity = createCapacity(currentRow, sheet.getRow(0));
            maximumCapacities.add(capacity);
        }

        workbook.close();
        return Flux.fromIterable(maximumCapacities);
    }

    private MinimumCapacity createCapacity(Row currentRow, Row header) {
        MinimumCapacity.MinimumCapacityBuilder builder = MinimumCapacity.builder();

        builder.name(getCellValueAsString(currentRow.getCell(0)));

        List<MinimumCapacityValue> capacities = new ArrayList<>();
        for (int i = 1; i < currentRow.getLastCellNum(); i++) {
            Cell cell = currentRow.getCell(i);
            if (cell != null) {
                MinimumCapacityValue capacityValue = convertCellToCapacityValue(cell, header.getCell(i));
                capacities.add(capacityValue);
            }
        }

        builder.capacity(capacities);

        return builder.build();

    }

    private MinimumCapacityValue convertCellToCapacityValue(Cell cell, Cell headerCell) {
        MinimumCapacityValue capacityValue = new MinimumCapacityValue();
        capacityValue.setDate(getCellValueAsLocalDate(headerCell));
        capacityValue.setValue(getCellValueAsBigDecimal(cell));
        return capacityValue;
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

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }

        return true;
    }
}
