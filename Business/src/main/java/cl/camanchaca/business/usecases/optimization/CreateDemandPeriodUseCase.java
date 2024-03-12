package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.optimization.DemandPeriodRepository;
import cl.camanchaca.domain.models.optimization.DemandPeriod;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@AllArgsConstructor
public class CreateDemandPeriodUseCase extends Usecase<List<DemandPeriod>, Flux<DemandPeriod>> {

    private final DemandPeriodRepository demandPeriodRepository;
    @Override
    public Flux<DemandPeriod> apply(List<DemandPeriod> demandPeriods) {
        return Flux.fromIterable(demandPeriods)
                .flatMap(demandPeriodRepository::save)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error al guardar los datos"));
                });
    }
}
