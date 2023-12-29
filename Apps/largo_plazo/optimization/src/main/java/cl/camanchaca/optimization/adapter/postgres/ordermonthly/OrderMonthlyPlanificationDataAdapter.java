package cl.camanchaca.optimization.adapter.postgres.ordermonthly;

import cl.camanchaca.business.repositories.OrderMonthlyPlanificationRepository;
import cl.camanchaca.domain.models.optimization.OrderMonthlyPlanification;
import cl.camanchaca.optimization.mapper.OrderMonthlyPlanificationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderMonthlyPlanificationDataAdapter implements OrderMonthlyPlanificationRepository {

    private final OrderMonthlyPlanificationDataRepository orderMonthlyPlanificationDataRepository;

    @Override
    public Mono<OrderMonthlyPlanification> getById(UUID id) {
        return orderMonthlyPlanificationDataRepository.findById(id)
                .map(OrderMonthlyPlanificationMapper::toModel);
    }

    @Override
    public Mono<OrderMonthlyPlanification> save(OrderMonthlyPlanification data) {
        return orderMonthlyPlanificationDataRepository
                .save(
                        OrderMonthlyPlanificationMapper.toData(data)
                )
                .map(OrderMonthlyPlanificationMapper::toModel);
    }
}
