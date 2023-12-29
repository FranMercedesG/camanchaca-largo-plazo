package cl.camanchaca.business.usecases.largoplazo.capacity.maximum;

import cl.camanchaca.business.repositories.MaximumCapacityRepository;
import cl.camanchaca.business.repositories.TotalMaximumRepository;
import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumTotalProductiveCapacity;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SaveAllTotalProductiveCapacityUseCase {

    private final TotalMaximumRepository totalMaximumRepository;

    private final MaximumCapacityRepository maximumRepository;

    public Mono<Void> apply(List<MaximumCapacity> body, Map<String, String> headerInfo) {
        return totalMaximumRepository.deleteAll()
                .thenMany(Flux.fromIterable(body)).collectList()
                .zipWith(maximumRepository.getAllProductiveCapacity().collectList())
                .flatMapMany(tuple -> {
                    List<MaximumCapacity> totalMaximum = tuple.getT1();
                    List<ProductiveCapacity> productiveCapacities = tuple.getT2();

                    Map<String, ProductiveCapacity> map = productiveCapacities.stream().collect(Collectors.toMap(ProductiveCapacity::getName, Function.identity()));

                    List<MaximumTotalProductiveCapacity> total = totalMaximum.stream()
                            .flatMap(maximumCapacity -> maximumCapacity.getCapacity().stream()
                                    .filter(c -> map.containsKey(maximumCapacity.getName()))
                                    .map(c -> MaximumTotalProductiveCapacity.builder()
                                            .productivecapacityid(map.get(maximumCapacity.getName()).getId())
                                            .name(maximumCapacity.getName())
                                            .maximumcapacity(c.getValue())
                                            .period(c.getDate())
                                            .build()))
                            .collect(Collectors.toList());

                    return totalMaximumRepository.saveAllTotalProductive(total);
                })
                .then();
    }
}
