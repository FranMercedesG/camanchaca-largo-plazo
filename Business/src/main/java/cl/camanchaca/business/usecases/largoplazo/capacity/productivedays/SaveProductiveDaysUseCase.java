package cl.camanchaca.business.usecases.largoplazo.capacity.productivedays;

import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class SaveProductiveDaysUseCase {

    private final BaseScenarioRepository baseScenarioRepository;

    public Flux<BasePeriodScenario> apply(List<BasePeriodScenario> o, Map<String, String> headerInfo) {
        return baseScenarioRepository.deleteAll().thenMany(baseScenarioRepository.saveAll(Flux.fromIterable(o)));
    }
}
