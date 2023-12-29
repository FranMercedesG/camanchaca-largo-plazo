package cl.camanchaca.business.usecases.largoplazo.biomass.bigquery;

import cl.camanchaca.business.repositories.DailyPlanificationRepository;
import cl.camanchaca.business.repositories.bigquery.RealBiomassBQRepository;
import cl.camanchaca.business.repositories.biomass.RealBiomassRepository;
import cl.camanchaca.domain.models.biomass.RealBiomass;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@AllArgsConstructor
public class GetRealBiomassBQUseCase implements Supplier<Flux<RealBiomass>> {

    private final RealBiomassBQRepository realBiomassBQRepository;
    private final RealBiomassRepository realBiomassRepository;
    private final DailyPlanificationRepository dailyPlanificationRepository;


    @Override
    public Flux<RealBiomass> get() {
            return realBiomassRepository
                    .deleteAll()
                    .then(dailyPlanificationRepository.getSelectPeriod())
                    .flatMapMany(planificationPeriod ->
                            realBiomassBQRepository.getByDate(planificationPeriod.getPlanificationDate())
                    )
                    .flatMap(realBiomass ->
                            realBiomassRepository.save(realBiomass), 3);

    }
}
