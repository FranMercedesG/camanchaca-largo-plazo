package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.generic.UpdateOptimizationBodyRequest;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.OrderMonthlyPlanificationRepository;
import cl.camanchaca.domain.models.optimization.OrderMonthlyPlanification;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class UpdateOptimizationUseCase extends Usecase<UpdateOptimizationBodyRequest, Mono<OrderMonthlyPlanification>> {
    private final OrderMonthlyPlanificationRepository orderMonthlyPlanificationRepository;
    @Override
    public Mono<OrderMonthlyPlanification> apply(UpdateOptimizationBodyRequest request) {
        return orderMonthlyPlanificationRepository
                .getById(request.getOrderMonthlyPlanificationId())
                .map(orderMonthlyPlanification -> orderMonthlyPlanification
                        .toBuilder()
                        .kiloWfe(request.getKilosWfe())
                        .build()
                )
                .flatMap(orderMonthlyPlanificationRepository::save);
    }
}
