package cl.camanchaca.business.repositories.bigquery;

import cl.camanchaca.domain.models.Order;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface SalmonOrdersBQRepository {

    Flux<Order> getOrdersBetweenDate(LocalDate initialDate,
                                     LocalDate finalDate);

}
