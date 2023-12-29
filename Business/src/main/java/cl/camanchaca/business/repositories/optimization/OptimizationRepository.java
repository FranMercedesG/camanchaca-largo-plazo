package cl.camanchaca.business.repositories.optimization;

import cl.camanchaca.domain.models.optimization.Optimization;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface OptimizationRepository {

    Flux<Optimization> getActiveByMonth(LocalDate initialDate, LocalDate finalDate);

    Flux<Optimization> getActiveByInitialAndFinalMonth(LocalDate initial, LocalDate finalMonth);


    Flux<Optimization> getAllByMonthAndVersion(LocalDate date, Integer version);


}
