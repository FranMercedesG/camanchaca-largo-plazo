package cl.camanchaca.business.usecases.largoplazo.capacity.minimum;

import cl.camanchaca.business.repositories.MinimumCapacityRepository;
import cl.camanchaca.business.repositories.TotalMinimumRepository;
import cl.camanchaca.domain.models.Minimum;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumTotalProductiveCapacity;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SaveAllTotalMinimumProductiveCapacityUseCase {

    private final TotalMinimumRepository totalMinimumRepository;

    private final MinimumCapacityRepository minimumCapacityRepository;
    public Mono<Void> apply(List<MinimumCapacity> o, Map<String, String> headerInfo) {
        return totalMinimumRepository.deleteAll()
                .thenMany(Flux.fromIterable(o)).collectList()
                .zipWith(minimumCapacityRepository.getAllProductiveCapacity().collectList())
                .flatMapMany(tuple -> {
                    List<MinimumCapacity> totalMaximum = tuple.getT1();
                    List<Minimum> productiveCapacities = tuple.getT2();

                    Map<String, Minimum> map = productiveCapacities.stream().collect(Collectors.toMap(Minimum::getName, Function.identity()));

                    List<MinimumTotalProductiveCapacity> total = totalMaximum.stream()
                            .flatMap(maximumCapacity -> maximumCapacity.getCapacity().stream()
                                    .filter(c -> map.containsKey(maximumCapacity.getName()))
                                    .map(c -> MinimumTotalProductiveCapacity.builder()
                                            .productivecapacityid(map.get(maximumCapacity.getName()).getId())
                                            .name(maximumCapacity.getName())
                                            .maximumcapacity(c.getValue())
                                            .period(c.getDate())
                                            .build()))
                            .collect(Collectors.toList());

                    return totalMinimumRepository.saveAllTotalProductive(total);
                })
                .then();
    }
}
