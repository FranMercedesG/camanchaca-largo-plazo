package cl.camanchaca.parametrization.adapter.excel;


import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.domain.models.parameters.ParameterPerformance;
import cl.camanchaca.domain.models.product.ProductPerformance;
import cl.camanchaca.parametrization.adapter.postgresql.product.ProductDataRepository;
import cl.camanchaca.parametrization.adapter.postgresql.size.SizeData;
import cl.camanchaca.parametrization.adapter.postgresql.size.SizeDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ExcelParameterPerformanceAdapter implements ExcelRepository<ProductPerformance> {
    private final SizeDataRepository sizeDataRepository;
    private final ProductDataRepository productDataRepository;

    @Override
    public Flux<ProductPerformance> readFile(InputStream file) {
        return sizeDataRepository.findAll()
                .map(SizeData::getSizeId)
                .collectList()
                .flatMapMany(uuids -> {
                    DataFormatter dataFormatter = new DataFormatter();
                    List<ProductPerformance> productSizes = new ArrayList<>();
                    try {
                        Workbook workbook = WorkbookFactory.create(file);
                        Sheet sheet = null;
                        for (Sheet sheetF : workbook) {
                            if (sheetF.getSheetName().startsWith("Rendimiento")) {
                                sheet = sheetF;
                                break;
                            }
                        }

                        boolean isFirtsRow = true;
                        for (Row row : sheet) {
                            if (isFirtsRow) {
                                isFirtsRow = false;
                                continue;
                            }
                            ProductPerformance productPerformance = new ProductPerformance();
                            List<ParameterPerformance> parameterPerformances = new ArrayList<>();

                            for (int i = 0; i <= uuids.size(); i++) {
                                Cell cell = row.getCell(i);

                                if (i == 0) {
                                    if (Objects.isNull(cell)) {
                                        break;
                                    }
                                    productPerformance.setProductId(
                                            (int) cell.getNumericCellValue()
                                    );
                                } else {

                                    String value = dataFormatter.formatCellValue(row.getCell(i + 1));
                                    BigDecimal performance = value.isBlank()
                                            ? BigDecimal.ZERO
                                            : BigDecimal.valueOf(
                                                    Double.valueOf(value)
                                    );
                                    parameterPerformances.add(ParameterPerformance.builder()
                                            .performance(performance)
                                            .columnId(uuids.get(i - 1))
                                            .build());
                                }
                            }
                            productSizes.add(productPerformance.toBuilder()
                                    .performances(parameterPerformances)
                                    .build());
                        }

                        workbook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Flux.fromIterable(productSizes);
                })
                .flatMap(productPerformance -> productDataRepository.findById(productPerformance.getProductId())
                        .map(data -> productPerformance.toBuilder()
                                .species(data.getEspecie())
                                .description(data.getDescripcion())
                                .build()), 5);


    }

}
