package cl.camanchaca.parametrization.mappers.util;

import cl.camanchaca.business.generic.BusinessError;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Objects;

public class ExcelUtil {


    private static final DataFormatter dataFormatter = new DataFormatter();


    public static DataFormatter dataFormatter() {
        return dataFormatter;
    }

    public static Workbook getWorkbook(InputStream file) {
        try {
            return WorkbookFactory.create(file);
        } catch (IOException e) {
            throw new RuntimeException("Error creating new workbook");
        }
    }


    public static Workbook createWorkbook(){
        return new XSSFWorkbook();
    }

    public static Mono<Boolean> isOne(Cell cell) {
        return Mono.defer(() -> validateNumberCell(dataFormatter.formatCellValue(cell)))
                .flatMap(value -> {
                    if (value < 0 || value > 1)
                        return Mono.error(new BusinessError("The values in the matrix can only be 1 or 0"));
                    return Mono.just(Objects.equals(value, 1));
                });

    }

    public static Mono<Integer> getIntValue(Cell cell) {
        return Mono.defer(() -> validateNumberCell(dataFormatter.formatCellValue(cell)));
    }

    public static Mono<BigDecimal> decimalValue(Cell cell) {
        return Mono.defer(() -> validatedecimalCell(dataFormatter.formatCellValue(cell)));
    }

    private static Mono<Integer> validateNumberCell(String value) {
        return validateIsNotEmptyValue(value)
                .flatMap(data -> {
                    try {
                        var valueInt = Integer.valueOf(data);
                        return Mono.just(valueInt);
                    } catch (NumberFormatException e) {
                        return Mono.error(new BusinessError("Format cell is invalid"));
                    }
                });
    }

    private static Mono<BigDecimal> validatedecimalCell(String value) {
        return validateIsNotEmptyValue(value)
                .flatMap(data -> {
                    try {
                        var valueInt = new BigDecimal(data.replace(",", "."));
                        return Mono.just(valueInt);
                    } catch (NumberFormatException e) {
                        return Mono.error(new BusinessError("Format cell is invalid"));
                    }
                });
    }

    private static Mono<String> validateIsNotEmptyValue(String value) {
        return Mono.defer(() ->
                value.isBlank()
                        ? Mono.empty()
                        : Mono.just(value)
        );
    }

    private ExcelUtil() {
    }
}
