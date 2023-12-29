package cl.camanchaca.business.usecases.shared;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ExcelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@AllArgsConstructor
public class ReadExcel<T> extends Usecase<InputStream, Flux<T>>{

    private final ExcelRepository<T> excelRepository;

    @Override
    public Flux<T> apply(InputStream excel) {
        try {
            return excelRepository.readFile(excel)
                    .onErrorResume(throwable -> {
                        log.error(throwable.getMessage(), throwable);
                        return Mono.error(new BusinessError(throwable.getMessage()));
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
