package cl.camanchaca.parametrization.validations;

import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import cl.camanchaca.generics.errors.InfrastructureError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class ValidationsCapacity {

    public static Flux<ParameterCapacityDTO> validateBodyCapacity(Flux<ParameterCapacityDTO> body) {
        return body.map(dto -> {
                    Stream.of(
                            dto.getProductiveCapacityId(),
                            dto.getProductId(),
                            dto.getStatus()
                    ).forEach(Objects::requireNonNull);
                    return dto;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }

    private ValidationsCapacity(){}
}
