package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.repositories.DownloadExcelRepository;
import cl.camanchaca.business.repositories.ParameterSizeRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.utils.DataRowExcel;
import cl.camanchaca.business.utils.ExcelDataToDownload;
import cl.camanchaca.domain.dtos.ParameterMinimumDTO;
import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DownloadParameterPerformanceUseCase {

    private final ProductRepository productRepository;
    private final ParameterSizeRepository parameterSizeRepository;
    private final SizeRepository sizeRepository;
    private final DownloadExcelRepository downloadExcelRepository;

    public Mono<byte[]> apply() {
        return getData()
                .zipWith(getHeaders())
                .map(tuple -> ExcelDataToDownload.of(tuple.getT2(), tuple.getT1(), "Performance"))
                .flatMap(downloadExcelRepository::generatedBytes);
    }

    private Mono<List<DataRowExcel>> getData() {
        return parameterSizeRepository.getAll()
                .collectMultimap(ParameterSizePerformanceDTO::getProductId)
                .map(mapDto -> mapDto
                        .keySet()
                        .stream()
                        .map(skuCode -> DataRowExcel
                                .builder()
                                .productId(skuCode)
                                .values(mapDto.get(skuCode)
                                        .stream()
                                        .collect(
                                                Collectors
                                                        .toMap(
                                                                dto -> dto.getSizeId().toString(),
                                                                dto -> dto.getPerformance().doubleValue(),
                                                                (existing, replacement) -> existing
                                                        )
                                        )
                                )
                                .build()
                        )
                        .collect(Collectors.toList())
                );

    }


    private Mono<Map<String, String>> getHeaders() {
        return sizeRepository.getAll()
                .collectList()
                .map(sizes -> {
                    var map = new HashMap<String, String>();
                    sizes.forEach(size -> map.put(size.getId().toString(), size.nameColumWithQualityAndSpecie()));
                    return new HashMap<>(map);
                });
    }
}
