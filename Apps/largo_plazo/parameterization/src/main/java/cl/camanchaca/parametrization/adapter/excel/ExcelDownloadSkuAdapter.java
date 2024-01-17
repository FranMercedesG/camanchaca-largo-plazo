package cl.camanchaca.parametrization.adapter.excel;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.repositories.DownloadSKUParameterRepository;
import cl.camanchaca.domain.models.Product;
import cl.camanchaca.parametrization.mappers.excel.ProductExcelMapper;
import cl.camanchaca.parametrization.mappers.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class ExcelDownloadSkuAdapter implements DownloadSKUParameterRepository {
    @Override
    public Mono<byte[]> getBytes(List<Product> data) {
        var headers = ProductExcelMapper.getHeaders();
        Workbook workbook = ExcelUtil.createWorkbook();
        Sheet sheet = workbook.createSheet("C STD");

        return getHeaderStyle(workbook)
                .zipWith(Mono.just(sheet.createRow(0)))
                .flatMapMany(fillHeaders(headers))
                .thenMany(fillData(headers, data, sheet))
                .then(writeBytes(workbook));
    }

    private static Mono<byte[]> writeBytes(Workbook workbook) {
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

    private static Flux<String> fillData(List<String> headers, List<Product> data, Sheet sheet) {
        return Flux.range(0, data.size())
                .flatMap(index -> {
                    var row = sheet.createRow(index + 1);
                    var values = ProductExcelMapper.toRow(data.get(index));
                    return Flux.fromIterable(headers)
                            .map(headers::indexOf)
                            .map(headerIndex -> {
                                var cell = row.createCell(headerIndex);
                                var value = values.get(headerIndex);
                                cell.setCellValue(value);
                                return value;
                            });
                });
    }

    private static Function<Tuple2<CellStyle, Row>, Publisher<? extends Cell>> fillHeaders(List<String> headers) {
        return tuple -> Flux.fromIterable(headers)
                .map(header -> {
                    var indexHeader = headers.indexOf(header);
                    Cell cell = tuple.getT2().createCell(indexHeader);
                    cell.setCellStyle(tuple.getT1());
                    cell.setCellValue(header);
                    return cell;
                });
    }


    private Mono<CellStyle> getHeaderStyle(Workbook workbook) {
        return Mono.defer(() -> {
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            return Mono.just(headerStyle);
        });
    }
}
