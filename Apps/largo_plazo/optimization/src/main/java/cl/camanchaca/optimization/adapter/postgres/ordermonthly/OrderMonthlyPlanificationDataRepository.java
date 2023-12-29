package cl.camanchaca.optimization.adapter.postgres.ordermonthly;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface OrderMonthlyPlanificationDataRepository extends ReactiveCrudRepository<OrderMonthlyPlanificationData, UUID> {
}
