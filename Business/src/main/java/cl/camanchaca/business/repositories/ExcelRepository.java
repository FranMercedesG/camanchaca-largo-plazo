package cl.camanchaca.business.repositories;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface ExcelRepository<T> {

    Flux<T> readFile(InputStream file) throws IOException;

}
