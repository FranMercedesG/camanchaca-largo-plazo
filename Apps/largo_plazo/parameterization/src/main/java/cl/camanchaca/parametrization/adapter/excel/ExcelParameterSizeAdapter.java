package cl.camanchaca.parametrization.adapter.excel;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.domain.models.parameters.ParameterSize;
import cl.camanchaca.domain.models.product.ProductSize;
import cl.camanchaca.parametrization.adapter.postgresql.product.ProductDataRepository;
import cl.camanchaca.parametrization.adapter.postgresql.size.SizeData;
import cl.camanchaca.parametrization.adapter.postgresql.size.SizeDataRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ExcelParameterSizeAdapter implements ExcelRepository<ProductSize> {
    private final SizeDataRepository sizeDataRepository;
    private final ProductDataRepository productDataRepository;
    @Override
    public Flux<ProductSize> readFile(InputStream file) {
        return sizeDataRepository.findAll()
                .map(SizeData::getSizeId)
                .collectList()
                .flatMapMany(uuids -> {
                    DataFormatter dataFormatter = new DataFormatter();
                    List<ProductSize> productSizes = new ArrayList<>();
                    try {
                        Workbook workbook = WorkbookFactory.create(file);
                        Sheet sheet = null;
                        for (Sheet sheetF : workbook) {
                            if (sheetF.getSheetName().startsWith("Calibres")) {
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
                            ProductSize productSize = new ProductSize();
                            List<ParameterSize> parameterSizeList = new ArrayList<>();

                            for (int i = 0; i <= uuids.size(); i++) {
                                Cell cell = row.getCell(i);

                                if (i == 0) {
                                    if (Objects.isNull(cell)) {
                                        break;
                                    }
                                    productSize.setProductId(
                                            (int) cell.getNumericCellValue()
                                    );
                                } else {
                                    String cellValue = dataFormatter.formatCellValue(row.getCell(i + 1));
                                    parameterSizeList.add(ParameterSize.builder()
                                            .status(!cellValue.isBlank())
                                            .columnId(uuids.get(i - 1))
                                            .build());
                                }
                            }
                            productSizes.add(productSize.toBuilder()
                                    .sizes(parameterSizeList)
                                    .build());
                        }

                        workbook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Flux.fromIterable(productSizes);
                })
                .flatMap(productSize -> productDataRepository.findById(productSize.getProductId())
                        .map(data -> productSize.toBuilder()
                                .species(data.getEspecie())
                                .description(data.getDescripcion())
                                .build()), 5);


    }


}
