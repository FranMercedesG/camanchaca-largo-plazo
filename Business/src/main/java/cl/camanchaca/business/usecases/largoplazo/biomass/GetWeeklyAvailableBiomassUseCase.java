package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.WeeklyPlanificationRepository;
import cl.camanchaca.business.repositories.biomass.AvailableBiomassRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;

@AllArgsConstructor
public class GetWeeklyAvailableBiomassUseCase extends Usecase<RequestParams, Mono<ParametersResponse>> {

    private final AvailableBiomassRepository availableBiomassRepository;
    private final WeeklyPlanificationRepository weeklyPlanificationRepository;

    @Override
    public Mono<ParametersResponse> apply(RequestParams params) {
        Integer offset = (params.getPage() - 1) * params.getSize();

        return weeklyPlanificationRepository.getWeeklyPlanificationPeriod()
                .flatMap(planificationPeriod -> availableBiomassRepository.getAllBetweenDateAndPage(
                                planificationPeriod.getPlanificationDate(),
                                getFinalDay(planificationPeriod.getPlanificationDate()),
                                params.getSize(),
                                offset)
                        .collectList()
                        .zipWith(availableBiomassRepository.countBetweenDate(
                                planificationPeriod.getPlanificationDate(),
                                getFinalDay(planificationPeriod.getPlanificationDate())
                        ))
                )
                .map(tuple -> ParametersResponse.of(tuple.getT1(), tuple.getT2()))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error buscando los datos"));
                });

    }

    private LocalDate getFinalDay(LocalDate date) {
        LocalDate finalDate = date;
        while (finalDate.getDayOfWeek() != DayOfWeek.FRIDAY) {
            finalDate = finalDate.plusDays(1);
        }
        return finalDate;
    }
}

