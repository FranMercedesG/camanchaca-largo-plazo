package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;
@AllArgsConstructor
public class GetSalesTypeUseCase implements Supplier<Flux<String>> {

    private final UnrestrictedDemandRepository unrestrictedDemandRepository;

    @Override
    public Flux<String> get() {
        return unrestrictedDemandRepository.getSalesType();
    }
}
