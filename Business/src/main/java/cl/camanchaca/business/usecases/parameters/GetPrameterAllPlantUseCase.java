package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.PlantRepository;
import cl.camanchaca.domain.models.Plant;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@AllArgsConstructor
public class GetPrameterAllPlantUseCase implements Supplier<Flux<Plant>> {

    private final PlantRepository plantRepository;

    @Override
    public Flux<Plant> get() {
        return plantRepository.getAll();
    }
}
