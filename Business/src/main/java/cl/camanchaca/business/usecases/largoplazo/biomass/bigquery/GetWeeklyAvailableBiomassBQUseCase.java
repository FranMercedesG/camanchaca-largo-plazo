package cl.camanchaca.business.usecases.largoplazo.biomass.bigquery;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.WeeklyPlanificationRepository;
import cl.camanchaca.business.repositories.bigquery.AvailableBiomassBQRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;

@AllArgsConstructor
public class GetWeeklyAvailableBiomassBQUseCase extends Usecase<RequestParams, Mono<ParametersResponse>> {

    private final AvailableBiomassBQRepository availableBiomassBQRepository;
    private final WeeklyPlanificationRepository weeklyPlanificationRepository;


    @Override
    public Mono<ParametersResponse> apply(RequestParams params) {

        Integer offset = (params.getPage() - 1) * params.getSize();
        return weeklyPlanificationRepository.getWeeklyPlanificationPeriod()
                .flatMap(planificationPeriod ->
                {
                    try {
                        return availableBiomassBQRepository
                                .getBetweenDateAndPage(
                                        planificationPeriod.getPlanificationDate(),
                                        getFinalDay(planificationPeriod.getPlanificationDate()),
                                        params.getSize(),
                                        offset)
                                .collectList()
                                .zipWith(availableBiomassBQRepository
                                        .countBetweemDate(planificationPeriod.getPlanificationDate(),
                                                getFinalDay(planificationPeriod.getPlanificationDate()))
                                );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                        return Mono.error(new BusinessError(e.getMessage()));
                    }
                })
                .map(tuple -> ParametersResponse.of(tuple.getT1(), tuple.getT2()));

    }

    private LocalDate getFinalDay(LocalDate date) {
        LocalDate finalDate = date;
        while (finalDate.getDayOfWeek() != DayOfWeek.FRIDAY) {
            finalDate = finalDate.plusDays(1);
        }
        return finalDate;
    }

}
