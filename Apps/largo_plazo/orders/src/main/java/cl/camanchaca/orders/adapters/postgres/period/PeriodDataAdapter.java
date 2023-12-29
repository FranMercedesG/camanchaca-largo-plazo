package cl.camanchaca.orders.adapters.postgres.period;

import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.models.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PeriodDataAdapter implements PeriodRepository {
    private final PeriodDataRepository periodDataRepository;

    @Override
    public Mono<Period> saveSelectedPeriod(LocalDate from, LocalDate to, String usuario) {
        return periodDataRepository.deleteAllByUser(usuario)
                .then(Mono.defer(() -> {
                    PeriodData periodData = PeriodData.builder()
                            .initialPeriod(from)
                            .finalPeriod(to)
                            .user(usuario)
                            .build();
                    return periodDataRepository.save(periodData);
                }))
                .map(savedPeriodData -> Period.builder()
                        .initialPeriod(savedPeriodData.getInitialPeriod())
                        .finalPeriod(savedPeriodData.getFinalPeriod())
                        .build());
    }

    @Override
    public Flux<Period> getSelectedPeriodByUser(String user) {
        return periodDataRepository.findAllByUser(user).map(savedPeriodData -> Period.builder()
                .initialPeriod(savedPeriodData.getInitialPeriod())
                .finalPeriod(savedPeriodData.getFinalPeriod())
                .build());
    }
}
