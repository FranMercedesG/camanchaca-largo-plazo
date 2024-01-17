package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.repositories.DownloadExcelRepository;
import cl.camanchaca.business.repositories.ParameterCapacityRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.ProductiveCapacityRepository;
import cl.camanchaca.business.utils.DataRowExcel;
import cl.camanchaca.business.utils.ExcelDataToDownload;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DownloadParameterCapacityUseCase {

    private final ProductRepository productRepository;

    private final ProductiveCapacityRepository productiveCapacityRepository;

    private final DownloadExcelRepository downloadExcelRepository;

    private final ParameterCapacityRepository parameterCapacityRepository;


    public Mono<byte[]> apply() {
        return getData()
                .zipWith(getHeaders())
                .map(tuple -> ExcelDataToDownload.of(tuple.getT2(), tuple.getT1(), "Capacity"))
                .flatMap(downloadExcelRepository::generatedBytes);
    }


    private Mono<List<DataRowExcel>> getData() {
        return parameterCapacityRepository.getAll()
                .collectMultimap(ParameterCapacityDTO::getProductId)
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
                                                                dto -> dto.getProductiveCapacityId().toString(),
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
    
    private Mono<Map<String, String>> getHeaders() {
        return productiveCapacityRepository.getAll()
                .collectList()
                .map(capacities -> {
                    var map = new HashMap<String, String>();
                    capacities.forEach(capacity -> map.put(capacity.getId().toString(), capacity.getName()));
                    return new HashMap<>(map);
                });
    }

    private double getValueStatus(boolean status) {
        return status ? 1 : 0;
    }
}
