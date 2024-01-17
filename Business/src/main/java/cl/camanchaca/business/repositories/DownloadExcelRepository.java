package cl.camanchaca.business.repositories;

import cl.camanchaca.business.utils.ExcelDataToDownload;
import reactor.core.publisher.Mono;

public interface DownloadExcelRepository {

    Mono<byte[]> generatedBytes(ExcelDataToDownload data);
}
