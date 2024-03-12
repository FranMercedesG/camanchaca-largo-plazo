package cl.camanchaca.optimization.adapter.postgres.demandperiod;

import cl.camanchaca.business.repositories.optimization.DemandPeriodRepository;
import cl.camanchaca.domain.models.optimization.DemandPeriod;
import cl.camanchaca.domain.models.optimization.Period;
import cl.camanchaca.optimization.adapter.postgres.demandperiod.client.DemandPeriodDataDBClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DemandPeriodAdapter implements DemandPeriodRepository {

    private final DemandPeriodDataDBClientRepository demandPeriodDataDBClientRepository;

    private final DemandPeriodDataRepository demandPeriodRepository;

    @Override
    public Flux<DemandPeriod> getAll() {
        return demandPeriodDataDBClientRepository.getAll();
    }

    @Override
    public Flux<DemandPeriod> getAllBetweenDate(LocalDate initialDate, LocalDate endDate) {
        return demandPeriodDataDBClientRepository.getAllBetweenDate(initialDate, endDate);
    }

    @Override
    public Flux<DemandPeriod> getAllByPageAndSizeBetweenDates(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String dv) {
        return demandPeriodDataDBClientRepository.getAllByPageAndSize(initialDate, finalDate, page, size, dv);
    }

    @Override
    public Mono<Long> countByDate(LocalDate startDate, LocalDate endDate, String dv) {
        return demandPeriodDataDBClientRepository.countDemandPeriodData(startDate, endDate, dv);
    }

    @Override
    public Mono<Long> count() {
        return demandPeriodRepository.count();
    }

    @Override
    public Flux<DemandPeriod> save(DemandPeriod demandPeriod) {
        return toDemandPeriodData(demandPeriod)
                .flatMap(demand -> {
                    if (demand.getDemandPeriodId() == null) {
                        return demandPeriodRepository.insertData(demand);
                    } else {
                        return demandPeriodRepository.existsById(demand.getDemandPeriodId())
                                .flatMap(exists -> exists ? demandPeriodRepository.save(demand) : demandPeriodRepository.insertData(demand));
                    }
                })
                .collectList()
                .flatMapMany(savedData -> updateDemandPeriodWithSavedData(demandPeriod, savedData));
    }

    @Override
    public Mono<Period> getPeriodByPeriodAndDemandUUID(LocalDate period, UUID demandUUID) {
        return demandPeriodRepository.findByUnrestrictedDemandIdAndPeriod(demandUUID, period)
                .map(demandPeriodData ->
                        Period.builder()
                                .demandPeriodId(demandPeriodData.getDemandPeriodId())
                                .period(demandPeriodData.getPeriod())
                                .status(demandPeriodData.getStatus())
                                .build()
                )
                .switchIfEmpty(Mono.defer(() -> Mono.just(
                        Period.builder()
                                .demandPeriodId(null)
                                .period(period)
                                .status(false)
                                .build()
                )));
    }

    @Override
    public Flux<Period> getPeriodByPeriodINAndDemandUUID(List<LocalDate> periods, UUID demandUUID) {
            return demandPeriodRepository.findByUnrestrictedDemandIdAndPeriods(demandUUID, periods)
                    .collectList()
                    .flatMapMany(existingPeriodData -> {
                        Map<LocalDate, Period> existingPeriodMap = existingPeriodData.stream()
                                .map(data ->
                                        Period.builder()
                                                .demandPeriodId(data.getDemandPeriodId())
                                                .period(data.getPeriod())
                                                .status(data.getStatus() != null ? data.getStatus() : false)
                                                .build())
                                .collect(Collectors.toMap(Period::getPeriod, Function.identity()));

                        return Flux.fromIterable(periods)
                                .map(period -> existingPeriodMap.getOrDefault(period,
                                        Period.builder()
                                                .demandPeriodId(null)
                                                .period(period)
                                                .status(false)
                                                .build()));
                    });

    }


    private Mono<DemandPeriod> updateDemandPeriodWithSavedData(DemandPeriod demandPeriod, List<DemandPeriodData> savedData) {
        List<Period> updatedPeriods = savedData.stream()
                .map(data -> Period.builder()
                        .demandPeriodId(data.getDemandPeriodId())
                        .period(data.getPeriod())
                        .status(data.getStatus())
                        .build())
                .collect(Collectors.toList());

        demandPeriod.setPeriods(updatedPeriods);

        return Mono.just(demandPeriod);
    }


    private static Flux<DemandPeriodData> toDemandPeriodData(DemandPeriod demandPeriod) {
        return Mono.just(demandPeriod)
                .flatMapMany(demand -> Flux.fromStream(demand
                        .getPeriods()
                        .stream()
                        .map(period -> DemandPeriodData
                                .builder()
                                .period(period.getPeriod())
                                .demandPeriodId(period.getDemandPeriodId())
                                .status(period.getStatus())
                                .unrestrictedDemandId(demand.getUnrestrictedDemandId())
                                .build()
                        )));
    }
}
