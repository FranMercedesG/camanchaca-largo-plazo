package cl.camanchaca.business.usecases.largoplazo.capacity.minimum;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.business.repositories.MinimumCapacityRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.dtos.capacity.CapacityObjDTO;
import cl.camanchaca.domain.models.Minimum;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacityValue;
import cl.camanchaca.domain.models.capacity.minimum.MinimumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumPeriodDailyProductiveCapacity;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@RequiredArgsConstructor
public class SaveAllMinimumProductiveCapacityUseCase {
    private final MinimumCapacityRepository minimumCapacityRepository;
    private final BaseScenarioRepository baseScenarioRepository;
    private final PeriodRepository periodRepository;

    public Mono<Void> apply(List<MinimumCapacity> minimumCapacities, Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get("user"))
                .collectList()
                .filter(periodsSelected -> !periodsSelected.isEmpty())
                .flatMap(periodsSelected -> {
                    Period periodSel = periodsSelected.get(0);

                    return baseScenarioRepository.getAllBetweenDates(periodSel.getInitialPeriod(),
                                    periodSel.getFinalPeriod())
                            .map(BasePeriodScenario::getPeriod)
                            .collectList()
                            .filter(periods -> !periods.isEmpty())
                            .flatMap(periods -> {
                                  return minimumCapacityRepository.getAllByPeriod(periods)
                                          .collectList()
                                          .flatMap(minCapacities -> {
                                              return minimumCapacityRepository.deleteAllPeriodDailyCapacityByUUID(
                                                              minCapacities.stream()
                                                                      .map(MinimumDailyProductiveCapacity::getPeriodDailyProductiveCapacityId)
                                                                      .collect(Collectors.toList())
                                                      )
                                                      .then(minimumCapacityRepository.deleteAllProductiveCapacityByUUID(
                                                              minCapacities.stream()
                                                                      .map(MinimumDailyProductiveCapacity::getProductiveCapacityId)
                                                                      .collect(Collectors.toList())
                                                      )).then(Mono.just(minCapacities));
                                          });
                            })
                            .thenMany(Flux.fromIterable(minimumCapacities))
                            .flatMap(minimumCapacity -> {
                                //sino existe un minimo, se crea..
                                return minimumCapacityRepository.getAllProductiveCapacity()
                                        .collectList()
                                        .flatMap(productiveCapacities -> {
                                            boolean nameExists = productiveCapacities.stream()
                                                    .anyMatch(pc -> pc.getName().equals(minimumCapacity.getName()));
                                            if (nameExists) {

                                                Minimum nameProductive = productiveCapacities.stream()
                                                        .filter(pc -> pc.getName().equals(minimumCapacity.getName()))
                                                        .findFirst()
                                                        .get();

                                                return Mono.just(CapacityObjDTO.builder()
                                                        .name(minimumCapacity.getName())
                                                        .uuid(nameProductive.getId())
                                                        .build());
                                            } else {
                                                return minimumCapacityRepository.saveProductiveCapacity(minimumCapacity.getName())
                                                        .flatMap(uuid -> Mono.just(CapacityObjDTO.builder()
                                                                .name(minimumCapacity.getName())
                                                                .uuid(uuid)
                                                                .build()));
                                            }
                                        });
                            })
                            .distinct()
                            .flatMap(uuid ->
                                    Flux.fromIterable(minimumCapacities).filter(o -> o.getName().equalsIgnoreCase(uuid.getName()))
                                        .flatMap(capacityObj -> {
                                    return minimumCapacityRepository.saveAllPeriodDailyCapacity(minimumCapacities.stream()
                                            .filter(minCap -> minCap.getName().equalsIgnoreCase(capacityObj.getName()))
                                            .flatMap(minCap -> minCap.getCapacity().stream()
                                                    .map(m -> MinimumPeriodDailyProductiveCapacity.builder()
                                                            .period(m.getDate())
                                                            .maximum(m.getValue())
                                                            .productiveCapacityId(uuid.getUuid())
                                                            .build()))
                                            .collect(Collectors.toList())
                                    );
                            }))
                            .then();
                });
    }
}
