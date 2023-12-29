package cl.camanchaca.business.usecases.largoplazo.orders;

import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@AllArgsConstructor
public class GetSaleStatusUseCase implements Supplier<Flux<String>> {

    private final UnrestrictedDemandRepository unrestrictedDemandRepository;
    @Override
    public Flux<String> get() {
        return unrestrictedDemandRepository.getAllSalesStatus();
    }

}
