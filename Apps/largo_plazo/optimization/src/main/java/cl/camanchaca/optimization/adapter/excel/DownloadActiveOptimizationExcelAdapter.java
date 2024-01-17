package cl.camanchaca.optimization.adapter.excel;

import cl.camanchaca.business.repositories.DownloadActiveOptimizationRepository;
import cl.camanchaca.domain.models.optimization.Optimization;
import cl.camanchaca.generics.errors.InfraestructureException;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DownloadActiveOptimizationExcelAdapter implements DownloadActiveOptimizationRepository {
    @Override
    public Mono<byte[]> downloadActiveOptimization(Flux<Optimization> optimization) {
        return optimization.collectList().flatMap(optimizationList -> {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Optimization");

                Row headerRow = sheet.createRow(0);

                XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
                headerStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 10, (byte) 10, (byte) 59}, null));
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setColor(IndexedColors.WHITE.getIndex());
                headerStyle.setFont(headerFont);


                String[] columnNames = {
                        "Periodo", "Kilo Wfe", "Calibre", "Codigo"
                };

                for (int i = 0; i < columnNames.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnNames[i]);
                    cell.setCellStyle(headerStyle);
                    sheet.autoSizeColumn(i);
                }

                if (optimizationList != null) {
                    for (int i = 0; i < optimizationList.size(); i++) {
                        Optimization optimizationObj = optimizationList.get(i);
                        Row dataRow = sheet.createRow(i + 1);
                        setCellValue(dataRow.createCell(0), optimizationObj.getPeriod());
                        setCellValue(dataRow.createCell(1), optimizationObj.getKiloWfe());
                        setCellValue(dataRow.createCell(2), optimizationObj.getSizeName());
                        setCellValue(dataRow.createCell(3), optimizationObj.getCodigo());
                    }
                }

                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    workbook.write(bos);
                    return Mono.just(bos.toByteArray());
                }
            } catch (IOException e) {
                return Mono.error(new InfraestructureException("Error al generar el archivo Excel"));
            }
        });
    }



    private void setCellValue(Cell cell, Object value) {
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) value).doubleValue());
            } else if (value instanceof LocalDate) {
                cell.setCellValue(((LocalDate) value).toString());
            } else {
                cell.setCellValue(value.toString());
            }
        } else {
            cell.setCellValue("");
        }
    }
}
