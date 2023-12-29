package cl.camanchaca.biomass.adapter.excel;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import cl.camanchaca.domain.models.biomass.PieceByDay;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectedBiomassInExcelAdapter implements ExcelRepository<ProjectedBiomassResponse> {

    @Override
    public Flux<ProjectedBiomassResponse> readFile(InputStream file) {

        return Flux.create(fluxSink -> {
            try {
                Workbook workbook = WorkbookFactory.create(file);
                Sheet sheet = workbook.getSheetAt(0);
                List<LocalDate> dates = getDates(sheet);
                DataFormatter dataFormatter = new DataFormatter();

                boolean isFirtsRow = true;
                for (Row row : sheet) {
                    if (isFirtsRow) {
                        isFirtsRow = false;
                        continue;
                    }
                    Cell quality = row.getCell(0);
                    Cell size = row.getCell(1);

                    ProjectedBiomassResponse data = ProjectedBiomassResponse.builder()
                            .quality(
                                    dataFormatter.formatCellValue(quality)
                            )
                            .size(
                                    dataFormatter.formatCellValue(size)
                            )
                            .days(new ArrayList<>())
                            .build();

                    for (int i = 0; i < dates.size(); i++) {
                        String value = dataFormatter.formatCellValue(row.getCell(i+2))
                                .replace(",", ".");
                        data.getDays().add(
                                PieceByDay.builder()
                                        .period(dates.get(i))
                                        .piecesBySizeValue(Long.valueOf(value))
                                        .build()
                        );
                    }
                    fluxSink.next(data);
                }

                fluxSink.complete();
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
                fluxSink.error(e);
            }
        });


    }


    private List<LocalDate> getDates(Sheet sheet) {

        Row headers = sheet.getRow(0);

        List<LocalDate> dates = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        for (int i = 2; i < 7; i++) {
            Cell cell = headers.getCell(i);
            String value = dataFormatter.formatCellValue(cell);

            if (value.isBlank()) {
                break;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fecha = LocalDate.parse(value, formatter);
            dates.add(fecha);
        }

        return dates;
    }


}
