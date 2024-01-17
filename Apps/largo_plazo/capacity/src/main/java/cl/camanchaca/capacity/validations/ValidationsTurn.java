package cl.camanchaca.capacity.validations;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.PlanificationPeriod;
import cl.camanchaca.utils.LocalDateUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class ValidationsTurn {

    public static Mono<Period> validateParams(
            ServerRequest request
    ) {
        return GenericValidations
                .validateQueryParam(request, "from")
                .zipWith(GenericValidations.validateQueryParam(request, "to"))
                .map(
                        tuple ->
                                Period.builder()
                                        .initialPeriod(LocalDateUtils.parseStringToDate(tuple.getT1()))
                                        .finalPeriod(LocalDateUtils.parseStringToDate(tuple.getT2()))
                                        .build()
                );
    }

    public static Mono<PlanificationPeriod> validationParameterTurn(
            ServerRequest request
    ) {
        return GenericValidations
                .validateQueryParam(request, "day")
                .map(day ->
                        PlanificationPeriod.builder()
                                .planificationDate(LocalDateUtils.parseStringToDate(day))
                                .build()
                );
    }

    private ValidationsTurn(){}

    public static void validateHeader(ServerRequest.Headers headers) {
        Stream.of(
                headers.header(Constans.USER.getValue()).get(0),
                headers.header(Constans.OFFICE.getValue()).get(0)
        ).forEach(Objects::requireNonNull);
    }
}
