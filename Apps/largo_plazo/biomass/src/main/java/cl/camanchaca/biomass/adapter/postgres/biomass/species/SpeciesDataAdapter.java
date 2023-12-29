package cl.camanchaca.biomass.adapter.postgres.biomass.species;


import cl.camanchaca.business.repositories.biomass.SpeciesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class SpeciesDataAdapter implements SpeciesRepository {

    private final SpeciesDataRepository speciesDataRepository;
    @Override
    public Flux<String> getAll() {
        return speciesDataRepository.findAllSpecies();
    }
}
