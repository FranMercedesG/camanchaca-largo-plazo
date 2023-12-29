package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.repositories.biomass.AvailableBiomassRepository;
import cl.camanchaca.business.repositories.biomass.RealBiomassRepository;
import cl.camanchaca.business.usecases.largoplazo.biomass.bigquery.GetDailyAvailableBiomassBQUseCase;
import cl.camanchaca.business.usecases.largoplazo.biomass.bigquery.GetRealBiomassBQUseCase;
import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import cl.camanchaca.domain.models.biomass.RealBiomass;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class RestoreBIomassUseCase {

    private final GetRealBiomassBQUseCase getRealBiomassBQUseCase;
    private final GetDailyAvailableBiomassBQUseCase getDailyAvailableBiomassBQUseCase;
    private final AvailableBiomassRepository availableBiomassRepository;
    private final RealBiomassRepository realBiomassRepository;

    public Mono<String> restore() throws InterruptedException {
        log.info("init restore");
        return availableBiomassRepository
                .deleteAll()
                .thenMany(this.saveBQAvailableBiomass())
                .thenMany(this.saveRealBiomassBQ())
                .then(Mono.just("Restore Complete"));

    }


    private Flux<AvailableBiomass> saveBQAvailableBiomass() {
        log.info("save available biomass");
        return getDailyAvailableBiomassBQUseCase
                .get()
                .map(availableBiomass -> {
                    log.debug("availableBiomass {} ", availableBiomass);
                    return availableBiomass;
                })
                .flatMap(availableBiomassRepository::saveAvailableBiomass, 3)
                .onErrorContinue((throwable, o) -> {
                    throwable.printStackTrace();
                });
    }

    private Flux<RealBiomass> saveRealBiomassBQ() {
        log.info("save real biomass");
        return getRealBiomassBQUseCase.get()
                .map(realBiomass -> {
                    log.debug("real {} ", realBiomass);
                    return realBiomass;
                })
                .flatMap(realBiomassRepository::save, 3)
                .onErrorContinue((throwable, o) -> {
                    throwable.printStackTrace();
                });
    }
}
