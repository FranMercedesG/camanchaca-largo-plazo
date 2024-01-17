package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.repositories.optimization.OptimizationRepository;
import cl.camanchaca.domain.models.optimization.Optimization;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.function.BiFunction;

@AllArgsConstructor
public class GetOptimizationByMonthAndVersionUseCase implements BiFunction<LocalDate, Integer, Flux<Optimization>> {

    private final OptimizationRepository optimizationRepository;
    private final SizeRepository sizeRepository;

    @Override
    public Flux<Optimization> apply(LocalDate date, Integer version) {
         return optimizationRepository
                .getAllByMonthAndVersion(date, version)  .flatMap(optimization -> sizeRepository
                         .getById(optimization.getSizeId())
                         .map(size -> optimization.toBuilder()
                                 .sizeName(size.nameColunm())
                                 .build()));
    }
}
