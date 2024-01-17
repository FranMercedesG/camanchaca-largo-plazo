package cl.camanchaca.capacity.validations;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacityValue;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacityValue;
import cl.camanchaca.generics.errors.InfraestructureException;
import cl.camanchaca.generics.errors.InfrastructureError;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class CapacityValidations {

    private CapacityValidations() {
    }

    public static Mono<RequestParams> validateParamsPagination(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam(Constans.PAGE_SIZE.getValue()).get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new InfraestructureException(Constans.PAGE_ERROR.getValue());
                    }
                    return params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .build();
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static void validateHeader(ServerRequest.Headers headers) {
        Stream.of(
                headers.header(Constans.USER.getValue()).get(0),
                headers.header(Constans.OFFICE.getValue()).get(0)
        ).forEach(Objects::requireNonNull);
    }

    public static Flux<BasePeriodScenario> validateRequestBody(Flux<BasePeriodScenario> body) {
        return body.map(dto -> {
                    Stream.of(
                            dto.getExtraDays(),
                            dto.getPeriod(),
                            dto.getWorkDays()
                    ).forEach(Objects::requireNonNull);
                    return dto;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }

    public static Flux<MaximumCapacity> validateMaximumCapacityRequestBody(Flux<MaximumCapacity> body) {

        return body.flatMap(dto -> {
            if (dto.getName() == null) {
                return Mono.error(new InfrastructureError("03"));
            }

            if (dto.getCapacity() == null || dto.getCapacity().isEmpty()) {
                return Mono.error(new InfrastructureError("03"));
            }

            for (MaximumCapacityValue capacityValue : dto.getCapacity()) {
                if (capacityValue.getDate() == null || capacityValue.getValue() == null) {
                    return Mono.error(new InfrastructureError("03"));
                }
            }

            return Mono.just(dto);
        });
    }

    public static Flux<MinimumCapacity> validateMinimumCapacityRequestBody(Flux<MinimumCapacity> body) {

        return body.flatMap(dto -> {
            if (dto.getName() == null) {
                return Mono.error(new InfrastructureError("03"));
            }

            if (dto.getCapacity() == null || dto.getCapacity().isEmpty()) {
                return Mono.error(new InfrastructureError("03"));
            }

            for (MinimumCapacityValue capacityValue : dto.getCapacity()) {
                if (capacityValue.getDate() == null || capacityValue.getValue() == null) {
                    return Mono.error(new InfrastructureError("03"));
                }
            }

            return Mono.just(dto);
        });
    }
}
