package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.MonthlyPlanificationRepository;
import cl.camanchaca.business.repositories.OrderMonthlyPlanificationRepository;
import cl.camanchaca.domain.models.optimization.MonthlyPlanification;
import cl.camanchaca.domain.models.optimization.OrderMonthlyPlanification;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ActiveOptimizationUseCase extends Usecase<List<UUID>, Mono<Long>> {

    private final MonthlyPlanificationRepository monthlyPlanificationRepository;
    private final OrderMonthlyPlanificationRepository orderMonthlyPlanificationRepository;

    @Override
    public Mono<Long> apply(List<UUID> ids) {
        return Flux.fromIterable(ids)
                .flatMap(orderMonthlyPlanificationRepository::getById)
                .map(OrderMonthlyPlanification::getMonthlyPlanificationId)
                .collectList()
                          .flatMap(uuids -> Flux.fromIterable(uuids)
                        .flatMap(monthlyPlanificationRepository::getById)
                        .concatMap(monthlyPlanification ->
                                monthlyPlanificationRepository.getByInitPeriodAndFinalPeriod(
                                        monthlyPlanification.getInitialPeriod(), monthlyPlanification.getFinalPeriod())
                        )
                        .map(monthlyPlanification -> setStatus(uuids, monthlyPlanification))
                        .concatMap(monthlyPlanificationRepository::save)
                        .count()
                )                ;
    }

    private MonthlyPlanification setStatus(List<UUID> ids, MonthlyPlanification data) {
        return ids.contains(data.getMonthlyPlanificationId())
                ? data.toBuilder()
                .active(true)
                .build()
                : data.toBuilder()
                .active(false)
                .build();
    }

}
