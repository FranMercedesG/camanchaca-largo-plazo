package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.repositories.DownloadExcelRepository;
import cl.camanchaca.business.repositories.MinimumCapacityRepository;
import cl.camanchaca.business.repositories.ParameterMinimumRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.utils.DataRowExcel;
import cl.camanchaca.business.utils.ExcelDataToDownload;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import cl.camanchaca.domain.dtos.ParameterMinimumDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DownloadParameterMinimumUseCase {

    private final ParameterMinimumRepository parameterMinimumRepository;
    private final ProductRepository productRepository;
    private final MinimumCapacityRepository minimumRepository;
    private final DownloadExcelRepository downloadExcelRepository;

    public Mono<byte[]> apply() {
        return getData()
                .zipWith(getHeaders())
                .map(tuple -> ExcelDataToDownload.of(tuple.getT2(), tuple.getT1(), "Minimum"))
                .flatMap(downloadExcelRepository::generatedBytes);
    }


    private Mono<Map<String, String>> getHeaders() {
        return minimumRepository.getAll()
                .collectList()
                .map(minimums -> {
                    var map = new HashMap<String, String>();
                    minimums.forEach(minimum -> map.put(minimum.getId().toString(), minimum.getName()));
                    return new HashMap<>(map);
                });
    }

    private Mono<List<DataRowExcel>> getData() {
        return parameterMinimumRepository.getAll()
                .collectMultimap(ParameterMinimumDTO::getProductId)
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
                                                                dto -> dto.getMinimumId().toString(),
                                                                dto -> getValueStatus(dto.getStatus()),
                                                                (existing, replacement) -> existing
                                                        )
                                        )
                                )
                                .build()
                        )
                        .collect(Collectors.toList())
                );

    }
    private double getValueStatus(boolean status) {
        return status ? 1 : 0;
    }


}
