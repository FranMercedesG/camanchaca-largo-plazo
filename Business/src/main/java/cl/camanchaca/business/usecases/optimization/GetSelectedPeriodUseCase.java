package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.models.Period;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RequiredArgsConstructor
public class GetSelectedPeriodUseCase {

    private final PeriodRepository periodRepository;
    public Mono<ParametersResponse>  apply(Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue()))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }
                    Period period = periods.get(0);
                    return Mono.just(ParametersResponse.of(getMonthsBetweenTwoDates(period.getInitialPeriod(), period.getFinalPeriod()), 1L));
                });
    }
    private List<LocalDate> getMonthsBetweenTwoDates(LocalDate initialDate, LocalDate finalDate) {
        List<LocalDate> months = new ArrayList<>();
        YearMonth currentMonth = YearMonth.from(initialDate);

        while (!currentMonth.isAfter(YearMonth.from(finalDate))) {
            months.add(currentMonth.atDay(1));
            currentMonth = currentMonth.plusMonths(1);
        }
        return months;
    }

}
