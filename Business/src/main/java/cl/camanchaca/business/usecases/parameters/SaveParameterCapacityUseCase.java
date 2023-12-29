package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ParameterCapacityRepository;
import cl.camanchaca.business.validations.ValidateByPlant;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class SaveParameterCapacityUseCase extends Usecase<List<ParameterCapacityDTO>, Flux<ParameterCapacityDTO>> {

    private ParameterCapacityRepository parameterCapacityRepository;
    private ValidateByPlant validateByPlant;

    @Override
    public Flux<ParameterCapacityDTO> apply(List<ParameterCapacityDTO> parameterCapacityDTOS) {
        return validateByPlant.availablePlantForCapacity(parameterCapacityDTOS)
                .flatMap(dto -> parameterCapacityRepository
                        .getByPlantIdAndProductIdAndProductiveCapacityIdId(
                                dto.getPlantId(),
                                dto.getProductId(),
                                dto.getProductiveCapacityId()
                        )
                        .next()
                        .map(save -> save.toBuilder()
                                .status(dto.getStatus())
                                .build())
                        .switchIfEmpty(Mono.just(dto)), 5)
                .flatMap(parameterCapacityRepository::save)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });


    }
}
