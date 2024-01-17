package cl.camanchaca.business.validations;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.repositories.ProductPlantRepository;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class ValidateByPlant {

    private final ProductPlantRepository productPlantRepository;

    public Flux<ParameterSizePerformanceDTO> availablePlantForSizePerformance(List<ParameterSizePerformanceDTO> parameterSizePerformanceDTOs) {
        return Flux.fromIterable(parameterSizePerformanceDTOs)
                .flatMap(performanceDTO -> productPlantRepository.getByProducttIdAndPlantId(performanceDTO.getProductId(), performanceDTO.getPlantId())
                        .flatMap(parameterPlantDTO -> {
                            if (Boolean.FALSE.equals(parameterPlantDTO.getStatus())) {
                                log.error(parameterPlantDTO.toString());
                                return Mono.error(new BusinessError("No se encuentra el sku relacionado con la planta"));
                            }
                            return Mono.just(parameterPlantDTO);
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            log.error(performanceDTO.toString());
                            return Mono.error(new BusinessError("No se encuentra el sku relacionado con la planta"));
                        }))
                        .map(o -> performanceDTO));
    }

    public Flux<ParameterCapacityDTO> availablePlantForCapacity(List<ParameterCapacityDTO> parameterCapacityDTOS) {
        return Flux.fromIterable(parameterCapacityDTOS)
                .flatMap(dto -> productPlantRepository.getByProducttIdAndPlantId(dto.getProductId(), dto.getPlantId())
                        .flatMap(parameterPlantDTO -> {
                            if (Boolean.FALSE.equals(parameterPlantDTO.getStatus())) {
                                log.error(parameterPlantDTO.toString());
                                return Mono.error(new BusinessError("No se encuentra el sku relacionado con la planta"));
                            }
                            return Mono.just(parameterPlantDTO);
                        })
                        .switchIfEmpty(Mono.defer(() -> {
                            log.error(dto.toString());
                            return Mono.error(new BusinessError("No se encuentra el sku relacionado con la planta"));
                        }))
                        .map(o -> dto));
    }

}
