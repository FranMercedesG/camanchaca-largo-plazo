package cl.camanchaca.biomass.adapter.excel;
import cl.camanchaca.domain.models.biomass.PieceByDay;
import cl.camanchaca.business.repositories.ExcelProjectedBiomassRepository;
import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class ProjectedBiomassOutExcelAdapter implements ExcelProjectedBiomassRepository {

    public byte[] generateExcel(List<ProjectedBiomassResponse> projectedBiomasses) {
        try {
            Workbook workbook = new XSSFWorkbook();

            List<String> dateHeaders = projectedBiomasses.get(0)
                    .getDays()
                    .stream()
                    .sorted(Comparator.comparing(PieceByDay::getPeriod))
                    .map(pieceByDay -> pieceByDay.getPeriod().toString())
                    .collect(Collectors.toList());

            generatedHeaders(workbook, dateHeaders);
            fillWorkbook(workbook, projectedBiomasses);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public Mono<byte[]> generatedBytesOfExcel(List<ProjectedBiomassResponse> projectedBiomasses) {
        return Mono.fromCallable(() -> generateExcel(projectedBiomasses));
    }

    private void fillWorkbook(Workbook workbook, List<ProjectedBiomassResponse> data) {
        Sheet sheet = workbook.getSheet("Biomasa_Proyectada");
        int rowNum = 1;
        for (ProjectedBiomassResponse rowData : data) {
            Row dataRow = sheet.createRow(rowNum++);
            Cell qualityCell = dataRow.createCell(0);
            Cell sizeCell = dataRow.createCell(1);
            qualityCell.setCellValue(rowData.getQuality());
            sizeCell.setCellValue(rowData.getSize());

            rowData.getDays().sort(Comparator.comparing(PieceByDay::getPeriod));

            IntStream.range(0, rowData.getDays().size())
                    .forEach(index -> {
                        Cell dataCell = dataRow.createCell(index + 2);
                        dataCell.setCellValue(
                                rowData.getDays()
                                        .get(index)
                                        .getPiecesBySizeValue()
                        );
                    });
        }
    }

    private void generatedHeaders(Workbook workbook, List<String> headers) {
        Sheet sheet = workbook.createSheet("Biomasa_Proyectada");

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        Row headerRow = sheet.createRow(0);
        Cell qualityCell = headerRow.createCell(0);
        Cell sizeCell = headerRow.createCell(1);
        qualityCell.setCellValue("Calidad");
        sizeCell.setCellValue("Calibre");
        qualityCell.setCellStyle(headerStyle);
        sizeCell.setCellStyle(headerStyle);
        for (int i = 0; i < headers.size(); i++) {
            Cell headerCell = headerRow.createCell(i + 2);
            headerCell.setCellValue(headers.get(i));
            headerCell.setCellStyle(headerStyle);
        }

    }

}
