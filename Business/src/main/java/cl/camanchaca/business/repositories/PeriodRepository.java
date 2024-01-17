package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Period;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface PeriodRepository {
    Mono<Period> saveSelectedPeriod(LocalDate from, LocalDate to, String usuario);

    Flux<Period> getSelectedPeriodByUser(String user);
}
