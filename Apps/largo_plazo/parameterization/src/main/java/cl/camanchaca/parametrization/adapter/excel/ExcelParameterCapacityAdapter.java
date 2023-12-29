package cl.camanchaca.parametrization.adapter.excel;


import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.domain.models.parameters.ParameterCapacity;
import cl.camanchaca.domain.models.product.ProductCapacity;
import cl.camanchaca.parametrization.adapter.postgresql.procutive.capacity.ProductiveCapacityDataRepository;
import cl.camanchaca.parametrization.adapter.postgresql.product.ProductDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ExcelParameterCapacityAdapter implements ExcelRepository<ProductCapacity> {
    private final ProductiveCapacityDataRepository productiveCapacityDataRepository;
    private final ProductDataRepository productDataRepository;

    @Override
    public Flux<ProductCapacity> readFile(InputStream file) {

        return readFile2(file)
                .flatMap(productCapacity -> productDataRepository.findById(productCapacity.getProductId())
                        .map(data -> productCapacity.toBuilder()
                                .species(data.getEspecie())
                                .description(data.getDescripcion())
                                .build()), 5);

    }


    private Flux<ProductCapacity> readFile2(InputStream file) {
        return productiveCapacityDataRepository.findAll()
                .map(productSizeData -> productSizeData.getProductiveCapacityId())
                .collectList()
                .flatMapMany(uuids -> {
                    DataFormatter dataFormatter = new DataFormatter();
                    List<ProductCapacity> productSizes = new ArrayList<>();
                    try {
                        Workbook workbook = WorkbookFactory.create(file);
                        Sheet sheet = null;
                        for (Sheet sheetF : workbook) {
                            if (sheetF.getSheetName().startsWith("Capacidad")) {
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
                            ProductCapacity productCapacity = new ProductCapacity();
                            List<ParameterCapacity> parameterCapacities = new ArrayList<>();

                            for (int i = 0; i <= uuids.size(); i++) {
                                Cell cell = row.getCell(i);

                                if (i == 0) {
                                    if (Objects.isNull(cell)) {
                                        break;
                                    }
                                    productCapacity.setProductId(
                                            (int) cell.getNumericCellValue()
                                    );
                                } else {
                                    String cellValue = dataFormatter.formatCellValue(row.getCell(i+1));
                                    parameterCapacities.add(ParameterCapacity.builder()
                                            .status(!cellValue.isBlank())
                                            .columnId(uuids.get(i - 1))
                                            .build());
                                }
                            }
                            productSizes.add(productCapacity.toBuilder()
                                    .capacities(parameterCapacities)
                                    .build());
                        }

                        workbook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Flux.fromIterable(productSizes);
                });
    }
}
