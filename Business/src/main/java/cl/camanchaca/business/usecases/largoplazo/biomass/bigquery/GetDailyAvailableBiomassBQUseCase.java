package cl.camanchaca.business.usecases.largoplazo.biomass.bigquery;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.repositories.DailyPlanificationRepository;
import cl.camanchaca.business.repositories.bigquery.AvailableBiomassBQRepository;
import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
public class GetDailyAvailableBiomassBQUseCase implements Supplier<Flux<AvailableBiomass>> {

    private final AvailableBiomassBQRepository availableBiomassBQRepository;
    private final DailyPlanificationRepository dailyPlanificationRepository;


    @Override
    public Flux<AvailableBiomass> get() {

        return dailyPlanificationRepository.getSelectPeriod()
                .flatMapMany(planificationPeriod ->
                {
                    try {
                        return availableBiomassBQRepository
                                .getByDate(
                                        planificationPeriod.getPlanificationDate()
                                );
                    } catch (InterruptedException e) {
                        log.error("Error in GetDailyAvailableBiomassBQUseCase", e);
                        Thread.currentThread().interrupt();
                        return Flux.error(new BusinessError(e.getMessage()));
                    }
                });

    }
}
