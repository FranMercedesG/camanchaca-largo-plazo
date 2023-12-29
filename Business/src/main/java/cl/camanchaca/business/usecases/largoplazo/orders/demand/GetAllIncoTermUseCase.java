package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.business.repositories.bigquery.OrderBQRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@AllArgsConstructor
public class GetAllIncoTermUseCase implements Supplier<Flux<String>> {
    private final OrderBQRepository orderBQRepository;

    @Override
    public Flux<String> get() {
        return orderBQRepository.getAllIncoTerms();
    }
}
