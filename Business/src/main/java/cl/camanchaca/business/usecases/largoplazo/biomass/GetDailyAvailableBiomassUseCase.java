package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.DailyPlanificationRepository;
import cl.camanchaca.business.repositories.biomass.AvailableBiomassRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GetDailyAvailableBiomassUseCase extends Usecase<RequestParams, Mono<ParametersResponse>> {

    private final AvailableBiomassRepository availableBiomassRepository;
    private final DailyPlanificationRepository dailyPlanificationRepository;

    @Override
    public Mono<ParametersResponse> apply(RequestParams params) {
        Integer offset = (params.getPage() - 1) * params.getSize();
        return dailyPlanificationRepository.getSelectPeriod()
                .flatMap(planificationPeriod -> availableBiomassRepository.getAllByDateAndPage(planificationPeriod.getPlanificationDate(),
                        params.getSize(),
                        offset)
                        .collectList()
                        .zipWith(availableBiomassRepository.countByDate(planificationPeriod.getPlanificationDate()))
                )
                .map(tuple -> ParametersResponse.of(tuple.getT1(), tuple.getT2()))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error buscando los datos"));
                });

    }
}
