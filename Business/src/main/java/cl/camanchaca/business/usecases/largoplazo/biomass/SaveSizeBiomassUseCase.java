package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.domain.dtos.biomass.SizeBiomassDTO;
import cl.camanchaca.domain.models.Size;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@AllArgsConstructor
public class SaveSizeBiomassUseCase extends Usecase<SizeBiomassDTO, Flux<Size>> {

    private final SizeRepository sizeRepository;

    @Override
    public Flux<Size> apply(SizeBiomassDTO sizes) {
        return Flux.fromIterable(sizes.getDeletes())
                .concatMap(sizeRepository::deleteById)
                .thenMany(sizeRepository.sizeAll(sizes.getData()))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error guardando calibres"));
                });
    }
}
