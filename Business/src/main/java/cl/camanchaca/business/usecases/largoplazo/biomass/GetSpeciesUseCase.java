package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.repositories.biomass.SpeciesRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@AllArgsConstructor
public class GetSpeciesUseCase implements Supplier<Flux<String>> {

    private final SpeciesRepository speciesRepository;

    @Override
    public Flux<String> get() {
        return speciesRepository.getAll();
    }
}
