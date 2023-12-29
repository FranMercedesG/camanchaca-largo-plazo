package cl.camanchaca.business.repositories.bigquery;

import cl.camanchaca.domain.models.biomass.ProjectedBiomassScenario;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface ProjectedBiomasBQRepository {
    Flux<ProjectedBiomassScenario> getAllByScenario(LocalDate initialDate, LocalDate finalDate, String scenario, String especie, Integer size, Integer offset) throws InterruptedException;

    Flux<ProjectedBiomassScenario> getAll(LocalDate initialDate, LocalDate finalDate, Integer size, Integer offset) throws InterruptedException;

    Flux<String> getAllScenarios(LocalDate initialDate, LocalDate finalDate, Integer size, Integer offset) throws InterruptedException;
}
