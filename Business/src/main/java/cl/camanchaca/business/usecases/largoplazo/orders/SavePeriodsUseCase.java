package cl.camanchaca.business.usecases.largoplazo.orders;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.models.Period;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class SavePeriodsUseCase  {

    private final PeriodRepository periodRepository;
    private final RestoreOrdersBQUseCase restoreBQOrdersUseCase;

    public Mono<Period> apply(Period requestParams, Map<String, String> header) {

        return periodRepository.saveSelectedPeriod(getInitialPeriod(requestParams.getInitialPeriod()),
                getFinalPeriod(requestParams.getFinalPeriod()),
                header.get(Constans.USER.getValue()))
                .map(newPeriod -> {
                    restoreBQOrdersUseCase.restoreDataOfBQ();
                    return newPeriod;
        });
    }

    private LocalDate getInitialPeriod(LocalDate fecha) {
        return fecha.withDayOfMonth(1);
    }

    private LocalDate getFinalPeriod(LocalDate fecha) {
        return fecha.withDayOfMonth(fecha.lengthOfMonth());
    }
}
