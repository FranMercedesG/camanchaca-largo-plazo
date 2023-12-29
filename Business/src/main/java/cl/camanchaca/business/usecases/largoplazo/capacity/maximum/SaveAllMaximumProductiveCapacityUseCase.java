package cl.camanchaca.business.usecases.largoplazo.capacity.maximum;

import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.business.repositories.MaximumCapacityRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.dtos.capacity.CapacityObjDTO;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumPeriodDailyProductiveCapacity;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SaveAllMaximumProductiveCapacityUseCase {

    private final MaximumCapacityRepository maximumCapacityRepository;

    private final BaseScenarioRepository baseScenarioRepository;

    private final PeriodRepository periodRepository;

    public Mono<Void> apply(List<MaximumCapacity> maximumCapacities, Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get("user"))
                .collectList()
                .flatMap(selectedPeriods -> {
                    if (selectedPeriods.isEmpty()) {
                        return Mono.empty();
                    }

                    Period currentPeriod = selectedPeriods.get(0);

                    return baseScenarioRepository.getAllBetweenDates(currentPeriod.getInitialPeriod(),
                                    currentPeriod.getFinalPeriod())
                            .map(BasePeriodScenario::getPeriod)
                            .collectList()
                            .flatMap(periods -> {
                                return maximumCapacityRepository.getAllByPeriod(periods)
                                        .collectList()
                                        .flatMap(maxCapacities -> {
                                            return maximumCapacityRepository.deleteAllPeriodDailyProductiveCapacityByUUID(
                                                    maxCapacities.stream()
                                                            .map(MaximumDailyProductiveCapacity::getPeriodDailyProductiveCapacityId)
                                                            .collect(Collectors.toList())
                                            ).then(maximumCapacityRepository.deleteAllProductiveCapacityByUUID(
                                                    maxCapacities.stream()
                                                            .map(MaximumDailyProductiveCapacity::getProductiveCapacityId)
                                                            .collect(Collectors.toList())
                                            )).then(Mono.just(maxCapacities));
                                        });
                            })
                            .thenMany(Flux.fromIterable(maximumCapacities))
                            .flatMap(maximumCapacity -> {
                                //si no existe un productive capacity, se crea...
                                return maximumCapacityRepository.getAllProductiveCapacity()
                                        .collectList()
                                        .flatMap(productiveCapacities -> {
                                            boolean nameExists = productiveCapacities.stream()
                                                    .anyMatch(pc -> pc.getName().equals(maximumCapacity.getName()));
                                            if (nameExists) {

                                               ProductiveCapacity nameProductive = productiveCapacities.stream()
                                                        .filter(pc -> pc.getName().equals(maximumCapacity.getName())).findFirst().get();
                                                return Mono.just(CapacityObjDTO
                                                        .builder()
                                                        .name(maximumCapacity.getName())
                                                        .uuid(nameProductive.getId())
                                                        .build());

                                            } else {
                                                return maximumCapacityRepository.saveProductiveCapacity(maximumCapacity.getName())
                                                        .flatMap(uuid -> Mono.just(CapacityObjDTO.builder()
                                                                .name(maximumCapacity.getName())
                                                                .uuid(uuid)
                                                                .build()));
                                            }
                                        });
                            })
                            .distinct()
                            .flatMap(uuid ->
                                    Flux.fromIterable(maximumCapacities).filter(o -> o.getName().equalsIgnoreCase(uuid.getName()))
                                    .flatMap(maximumCapacity -> {
                                return maximumCapacityRepository.saveAllPeriodDailyProductiveCapacity(
                                        maximumCapacity.getCapacity().stream()
                                                .map(m -> MaximumPeriodDailyProductiveCapacity.builder()
                                                        .period(m.getDate())
                                                        .maximum(m.getValue())
                                                        .productiveCapacityId(uuid.getUuid())
                                                        .build())
                                                .collect(Collectors.toList())
                                );
                            }))
                            .then();
                });
    }
}




