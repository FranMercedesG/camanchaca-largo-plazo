package cl.camanchaca.optimization.adapter.postgres.monthly;

import cl.camanchaca.business.repositories.MonthlyPlanificationRepository;
import cl.camanchaca.domain.models.optimization.MonthlyPlanification;
import cl.camanchaca.optimization.mapper.MonthlyPlanificationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MonthlyPlanificationDataAdapter implements MonthlyPlanificationRepository {

    private final MonthlyPlanificationDataRepository monthlyPlanificationDataRepository;
    @Override
    public Flux<MonthlyPlanification> findByInitialAndFinalPeriod(LocalDate initialPeriod, LocalDate finalPeriod) {
        return monthlyPlanificationDataRepository.findAllByInitialPeriodAndFinalPeriod(initialPeriod, finalPeriod).map(MonthlyPlanificationMapper::toModel);
    }

    @Override
    public Flux<MonthlyPlanification> getByInitPeriodAndFinalPeriod(LocalDate initPeriod, LocalDate finalPeriod) {
        return monthlyPlanificationDataRepository
                .findAllByInitialPeriodAndFinalPeriod(initPeriod, finalPeriod)
                .map(MonthlyPlanificationMapper::toModel);
    }

    @Override
    public Mono<MonthlyPlanification> getById(UUID id) {
        return monthlyPlanificationDataRepository
                .findById(id)
                .map(MonthlyPlanificationMapper::toModel);
    }

    @Override
    public Mono<MonthlyPlanification> save(MonthlyPlanification data) {
        return monthlyPlanificationDataRepository
                .save(
                        MonthlyPlanificationMapper.toData(data)
                )
                .map(MonthlyPlanificationMapper::toModel);
    }
}
