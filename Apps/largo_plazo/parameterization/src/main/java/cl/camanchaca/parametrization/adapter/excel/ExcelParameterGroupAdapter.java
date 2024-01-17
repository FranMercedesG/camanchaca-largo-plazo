package cl.camanchaca.parametrization.adapter.excel;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.domain.models.parameters.ParameterGroup;
import cl.camanchaca.domain.models.product.ProductGroup;
import cl.camanchaca.parametrization.adapter.postgresql.group.GroupData;
import cl.camanchaca.parametrization.adapter.postgresql.group.GroupDataRepository;
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
public class ExcelParameterGroupAdapter implements ExcelRepository<ProductGroup> {

    private final GroupDataRepository groupDataRepository;
    private final ProductDataRepository productDataRepository;
    @Override
    public Flux<ProductGroup> readFile(InputStream file) {
        return groupDataRepository.findAll()
                .map(GroupData::getGroupId)
                .collectList()
                .flatMapMany(uuids -> {
                    DataFormatter dataFormatter = new DataFormatter();
                    List<ProductGroup> productGroups = new ArrayList<>();
                    try {
                        Workbook workbook = WorkbookFactory.create(file);
                        Sheet sheet = null;
                        for (Sheet sheetF : workbook) {
                            if (sheetF.getSheetName().startsWith("Grupos")) {
                                sheet = sheetF;
                                break;
                            }
                        }

                        Row header = sheet.getRow(0);

                        boolean isFirtsRow = true;
                        for (Row row : sheet) {
                            if (isFirtsRow) {
                                isFirtsRow = false;
                                continue;
                            }
                            ProductGroup productGroup = new ProductGroup();
                            List<ParameterGroup> parameterGroups = new ArrayList<>();

                            for (int i = 0; i <= uuids.size(); i++) {
                                Cell cell = row.getCell(i);

                                if (i == 0) {
                                    if (Objects.isNull(cell)) {
                                        break;
                                    }
                                    productGroup.setProductId(
                                            (int) cell.getNumericCellValue()
                                    );
                                } else {
                                    String cellValue = dataFormatter.formatCellValue(row.getCell(i + 1));
                                    parameterGroups.add(ParameterGroup.builder()
                                            .status(!cellValue.isBlank())
                                            .columnId(uuids.get(i - 1))
                                            .name(dataFormatter.formatCellValue(header.getCell(i + 1)))
                                            .build());
                                }
                            }
                            productGroups.add(productGroup.toBuilder()
                                    .groups(parameterGroups)
                                    .build());
                        }

                        workbook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Flux.fromIterable(productGroups);
                })
                .flatMap(productSize -> productDataRepository.findById(productSize.getProductId())
                        .map(data -> productSize.toBuilder()
                                .species(data.getEspecie())
                                .description(data.getDescripcion())
                                .build()), 5);


    }


}
