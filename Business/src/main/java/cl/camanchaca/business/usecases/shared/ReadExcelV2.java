package cl.camanchaca.business.usecases.shared;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.BusinessException;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ExcelRepository;
import cl.camanchaca.business.repositories.optimization.BulkExcelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class ReadExcelV2<T> extends Usecase<Map<String, Object>, Flux<T>> {


    private final BulkExcelRepository<T> excelRepository;

    @Override
    public Flux<T> apply(Map<String, Object> parameters) {
        InputStream excel = (InputStream) parameters.get("excel");
        Map<String, String> headers = (Map<String, String>) parameters.get("headers");
        try {
            return excelRepository.readFile(excel, headers)
                    .onErrorResume(throwable -> {
                        log.error(throwable.getMessage(), throwable);
                        return Mono.error(new BusinessError(throwable.getMessage()));
                    });
        } catch (IOException e) {
            throw new BusinessException(e.getLocalizedMessage());
        }
    }
}

