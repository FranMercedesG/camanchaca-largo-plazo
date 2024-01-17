package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.repositories.DownloadExcelRepository;
import cl.camanchaca.business.repositories.GroupRepository;
import cl.camanchaca.business.repositories.ParameterGroupRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.utils.DataRowExcel;
import cl.camanchaca.business.utils.ExcelDataToDownload;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DownloadParameterGroupUseCase {

    private final ProductRepository productRepository;
    private final ParameterGroupRepository parameterGroupRepository;
    private final GroupRepository groupRepository;
    private final DownloadExcelRepository downloadExcelRepository;

    public Mono<byte[]> apply() {
        return getData()
                .zipWith(getHeaders())
                .map(tuple -> ExcelDataToDownload.of(tuple.getT2(), tuple.getT1(), "Minimum"))
                .flatMap(downloadExcelRepository::generatedBytes);
    }

    private Mono<List<DataRowExcel>> getData() {
        return parameterGroupRepository.getAll()
                .collectMultimap(ParameterGroupDTO::getProductId)
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
                                                                dto -> dto.getGroupId().toString(),
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
        return groupRepository.getAll()
                .collectList()
                .map(groups -> {
                    var map = new HashMap<String, String>();
                    groups.forEach(group -> map.put(group.getId().toString(), group.getName()));
                    return new HashMap<>(map);
                });
    }

    private double getValueStatus(boolean status) {
        return status ? 1 : 0;
    }
}
