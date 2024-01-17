package cl.camanchaca.parametrization.adapter.excel;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.repositories.DownloadExcelRepository;
import cl.camanchaca.business.utils.ExcelDataToDownload;
import cl.camanchaca.parametrization.mappers.util.ExcelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
public class ExcelDownloadAdapter implements DownloadExcelRepository {

    private static final int INDEX_COLUM_DATA = 1;

    @Override
    public Mono<byte[]> generatedBytes(ExcelDataToDownload data) {

        Workbook workbook = ExcelUtil.createWorkbook();
        Sheet sheet = workbook.createSheet(data.getSheetName());
        var dataHeader = data.getHeaders().keySet().toArray();
        return getHeaderStyle(workbook)
                .zipWith(initHeaderRow(workbook, sheet))
                .flatMapMany(fillHeaders(data, dataHeader))
                .thenMany(Flux.range(1, data.getRows().size()))
                .flatMap(index -> Mono.just(sheet.createRow(index))
                        .zipWith(Mono.just(index))
                )
                .flatMap(fillData(data, dataHeader))
                .then(closeWoorbook(workbook));


    }

    private static Mono<byte[]> closeWoorbook(Workbook workbook) {
        return Mono.defer(() -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                workbook.write(outputStream);
                outputStream.close();
                return Mono.just(outputStream.toByteArray());

            } catch (IOException e) {
                log.error("Error while writing document", e);
                return Mono.error(new BusinessError("Error while writing document"));
            }

        });
    }

    private static Function<Tuple2<Row, Integer>, Publisher<? extends Cell>> fillData(ExcelDataToDownload data, Object[] dataHeader) {
        return tuple -> {
            var values = data.getRows().get(tuple.getT2() - 1).getValues();

            var row = data.getRows().get(tuple.getT2() - 1);

            Cell cellCode = tuple.getT1().createCell(0);

            cellCode.setCellValue(row.getProductId());
            return fillRow(dataHeader, tuple, values);
        };
    }

    private static Flux<Cell> fillRow(Object[] dataHeader, Tuple2<Row, Integer> tuple, Map<String, Double> values) {
        return Flux.range(0, dataHeader.length)
                .map(index -> {
                    var headerId = dataHeader[index];
                    Cell cell = tuple.getT1().createCell(index + INDEX_COLUM_DATA);
                    var value = Objects.requireNonNullElse(values.get(headerId), 0d);
                    cell.setCellValue(value);
                    return cell;
                });
    }

    private static Function<Tuple2<CellStyle, Row>, Publisher<? extends Cell>> fillHeaders(ExcelDataToDownload data, Object[] dataHeader) {
        return tuple -> Flux.range(0, data.getHeaders().size())
                .map(index -> {
                    var headerId = dataHeader[index];
                    var header = data.getHeaders().get(headerId);
                    Cell cell = tuple.getT2().createCell(index + INDEX_COLUM_DATA);
                    cell.setCellValue(header);
                    cell.setCellStyle(tuple.getT1());
                    return cell;
                });
    }

    private Mono<CellStyle> getHeaderStyle(Workbook workbook){
        return Mono.defer(() -> {
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            return Mono.just(headerStyle);
        });
    }


    private Mono<Row> initHeaderRow(Workbook workbook, Sheet sheet){
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        Row headerRow = sheet.createRow(0);
        var codeCell = headerRow.createCell(0);
        codeCell.setCellValue("Code");
        codeCell.setCellStyle(headerStyle);

        return Mono.just(headerRow);
    }
}
