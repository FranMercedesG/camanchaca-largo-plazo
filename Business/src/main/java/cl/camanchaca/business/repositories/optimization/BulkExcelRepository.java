package cl.camanchaca.business.repositories.optimization;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface BulkExcelRepository<T> {
    Flux<T> readFile(InputStream file, Map<String, String> headers) throws IOException;

}
