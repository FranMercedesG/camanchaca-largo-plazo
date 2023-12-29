package cl.camanchaca.capacity.adapters.postgres.baseperiodscenario;

import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.capacity.mappers.BasePeriodScenarioMapper;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BasePeriodScenarioDataAdapter implements BaseScenarioRepository {

    private final BasePeriodScenarioDataRepository repository;

    @Override
    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    @Override
    public Flux<BasePeriodScenario> getAll() {
        return repository.findAll().map(BasePeriodScenarioMapper::toBasePeriodScenario);
    }

    @Override
    public Flux<BasePeriodScenario> getAllBetweenDatesAndPageAndSize(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate) {
        return repository.findAllByDateRangeAndOffsetAndSize(initialDate, finalDate, page, size).map(BasePeriodScenarioMapper::toBasePeriodScenario);
    }

    @Override
    public Flux<BasePeriodScenario> getAllBetweenDates(LocalDate initialDate, LocalDate finalDate) {
        return repository.findAllByDateRange(initialDate, finalDate).map(BasePeriodScenarioMapper::toBasePeriodScenario);
    }

    @Override
    public Flux<BasePeriodScenario> saveAll(Flux<BasePeriodScenario> body) {
        return repository.saveAll(body.map(BasePeriodScenarioMapper::toBasePeriodScenarioData)).map(BasePeriodScenarioMapper::toBasePeriodScenario);
    }
}
