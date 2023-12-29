package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.optimization.OrderMonthlyPlanification;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderMonthlyPlanificationRepository {
    Mono<OrderMonthlyPlanification> getById(UUID id);
    Mono<OrderMonthlyPlanification> save(OrderMonthlyPlanification data);

}
