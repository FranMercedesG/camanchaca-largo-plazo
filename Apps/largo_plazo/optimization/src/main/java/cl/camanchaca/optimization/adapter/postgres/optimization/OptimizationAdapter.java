package cl.camanchaca.optimization.adapter.postgres.optimization;

import cl.camanchaca.business.repositories.optimization.OptimizationRepository;
import cl.camanchaca.domain.models.optimization.Optimization;
import cl.camanchaca.optimization.mapper.OptimizationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class OptimizationAdapter implements OptimizationRepository {

    private final OptimizationDataRepository optimizationDataRepository;
    @Override
    public Flux<Optimization> getActiveByMonth(LocalDate initialDate, LocalDate finalDate) {
        return optimizationDataRepository
                .getActiveByMonth(initialDate, finalDate)
                .map(OptimizationMapper::toOptimization);
    }

    @Override
    public Flux<Optimization> getActiveByInitialAndFinalMonth(LocalDate initial, LocalDate finalMonth) {
        return optimizationDataRepository
                .getActiveByMonth(initial, finalMonth)
                .map(OptimizationMapper::toOptimization);
    }

    @Override
    public Flux<Optimization> getAllByMonthAndVersion(LocalDate date, Integer version) {
        LocalDate[] dates = getFromAndUntilPerMonth(date);
        return optimizationDataRepository
                .getByMonthAndVersion(version, dates[0], dates[1])
                .map(OptimizationMapper::toOptimization);
    }

    private LocalDate[] getFromAndUntilPerMonth(LocalDate date) {
        LocalDate[] data = {
                date.withDayOfMonth(1), date.withDayOfMonth(
                date.lengthOfMonth()
        )};
        return data;
    }


}

