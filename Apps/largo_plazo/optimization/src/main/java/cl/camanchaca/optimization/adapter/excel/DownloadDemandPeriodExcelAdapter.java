package cl.camanchaca.optimization.adapter.excel;

import cl.camanchaca.business.repositories.optimization.DownloadDemandPeriodRepository;
import cl.camanchaca.domain.models.optimization.DemandPeriod;
import cl.camanchaca.domain.models.optimization.Period;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DownloadDemandPeriodExcelAdapter implements DownloadDemandPeriodRepository {

    @Override
    public Mono<byte[]> dowloadPeriodExcel(Flux<DemandPeriod> demandPeriodFlux, Mono<List<LocalDate>> datesMono) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Demand_Periods");

        return datesMono.flatMap(dates -> {
            Row headerRow = sheet.createRow(0);
            String[] fixedHeaders = {"Demand UUID", "Codigo", "Oficina", "Family", "Pais Destino", "DV"};
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            int cellIndex = 0;
            for (String header : fixedHeaders) {
                headerRow.createCell(cellIndex++).setCellValue(header);
            }
            for (LocalDate date : dates) {
                headerRow.createCell(cellIndex++).setCellValue(date.format(formatter));
            }

            return demandPeriodFlux.doOnNext(demandPeriod -> {
                int lastRowNum = sheet.getLastRowNum() + 1;
                Row row = sheet.createRow(lastRowNum);
                row.createCell(0).setCellValue(String.valueOf(demandPeriod.getUnrestrictedDemandId()));
                row.createCell(1).setCellValue(demandPeriod.getCodigo());
                row.createCell(2).setCellValue(demandPeriod.getOficina());
                row.createCell(3).setCellValue(demandPeriod.getFamilia());
                row.createCell(4).setCellValue(demandPeriod.getPais());
                row.createCell(5).setCellValue(demandPeriod.getDv());

                Map<LocalDate, Boolean> periodMap = demandPeriod.getPeriods().stream()
                        .collect(Collectors.toMap(Period::getPeriod, Period::getStatus));

                int dateCellIndex = fixedHeaders.length;
                for (LocalDate date : dates) {
                    // Verificar si existe un Period para la fecha y asignar el valor correspondiente
                    Boolean status = periodMap.getOrDefault(date, false);
                    String value = status ? "X" : "";
                    row.createCell(dateCellIndex++).setCellValue(value);
                }
            }).then(Mono.fromCallable(() -> {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                workbook.write(bos);
                workbook.close();
                return bos.toByteArray();
            })).onErrorMap(IOException::new);
        });
    }
}
