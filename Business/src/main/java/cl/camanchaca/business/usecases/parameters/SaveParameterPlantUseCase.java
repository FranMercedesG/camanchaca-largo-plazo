package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ProductPlantRepository;
import cl.camanchaca.domain.dtos.ParameterPlantDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class SaveParameterPlantUseCase extends Usecase<Flux<ParameterPlantDTO>, Flux<ParameterPlantDTO>> {

    private final ProductPlantRepository productPlantRepository;

    @Override
    public Flux<ParameterPlantDTO> apply(Flux<ParameterPlantDTO> parameterPlantDTOFlux) {
        return productPlantRepository.saveAll(parameterPlantDTOFlux)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });
    }
}
