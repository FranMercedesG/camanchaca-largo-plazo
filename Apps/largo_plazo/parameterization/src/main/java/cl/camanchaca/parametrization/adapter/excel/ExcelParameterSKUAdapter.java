package cl.camanchaca.parametrization.adapter.excel;

import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.domain.models.Product;
import cl.camanchaca.parametrization.mappers.ProductExcelParameterSKUMapper;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Iterator;

@Service
public class ExcelParameterSKUAdapter implements ExcelRepository<Product> {
    @Override
    public Flux<Product> readFile(InputStream file) {
        return Flux.create(emitter -> {
            Product product;
            try {
                Workbook workbook = WorkbookFactory.create(file);
                Sheet sheet = null;
                for (Sheet sheetF : workbook) {
                    if (sheetF.getSheetName().startsWith("C STD")) {
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
                    Iterator<Cell> cellIterator = row.cellIterator();
                    product = ProductExcelParameterSKUMapper.rowToProduct(cellIterator, new Product());
                    emitter.next(product);
                }

                workbook.close();
                emitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.error(e);
            }
        });

    }


}
