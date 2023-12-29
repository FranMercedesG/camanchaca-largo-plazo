package cl.camanchaca.parametrization.adapter.excel;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.domain.models.parameters.ParameterMinimum;
import cl.camanchaca.domain.models.product.*;
import cl.camanchaca.parametrization.adapter.postgresql.minimum.MinimumData;
import cl.camanchaca.parametrization.adapter.postgresql.minimum.MinimumDataRepository;
import cl.camanchaca.parametrization.adapter.postgresql.product.ProductDataRepository;
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
public class ExcelParameterMinimumAdapter implements ExcelRepository<ProductMinimum> {

    private final MinimumDataRepository minimumDataRepository;
    private final ProductDataRepository productDataRepository;

    @Override
    public Flux<ProductMinimum> readFile(InputStream file) {
        return minimumDataRepository.findAll()
                .map(MinimumData::getMinimumId)
                .collectList()
                .flatMapMany(uuids -> {
                    DataFormatter dataFormatter = new DataFormatter();
                    List<ProductMinimum> productMinimums = new ArrayList<>();
                    try {
                        Workbook workbook = WorkbookFactory.create(file);
                        Sheet sheet = null;
                        for (Sheet sheetF : workbook) {
                            if (sheetF.getSheetName().startsWith("Minimos")) {
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

                            ProductMinimum productMinimum = new ProductMinimum();
                            List<ParameterMinimum> parameterMinimums = new ArrayList<>();

                            for (int i = 0; i <= uuids.size(); i++) {

                                Cell cell = row.getCell(i);

                                if (i == 0) {

                                    if (Objects.isNull(cell)) {
                                        break;
                                    }
                                    
                                    productMinimum.setProductId(
                                            (int) cell.getNumericCellValue()
                                    );
                                } else {

                                    String cellValue = dataFormatter.formatCellValue(row.getCell(i + 1));

                                    parameterMinimums.add(ParameterMinimum.builder()
                                            .status(!cellValue.isBlank())
                                            .columnId(uuids.get(i - 1))
                                            .build());
                                }
                            }
                            productMinimums.add(productMinimum.toBuilder()
                                    .minimums(parameterMinimums)
                                    .build());
                        }

                        workbook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Flux.fromIterable(productMinimums);
                })
                .flatMap(productSize -> productDataRepository.findById(productSize.getProductId())
                        .map(data -> productSize.toBuilder()
                                .species(data.getEspecie())
                                .description(data.getDescripcion())
                                .build()), 5);


    }


}
