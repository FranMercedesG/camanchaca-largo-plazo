package cl.camanchaca.parametrization.validations;

import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import cl.camanchaca.domain.dtos.group.GroupSKUParameterDTO;
import cl.camanchaca.generics.errors.InfrastructureError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class ValidationsGroup {

    public static Flux<ParameterGroupDTO> validateBodyGroup(Flux<ParameterGroupDTO> body) {
        return body.map(dto -> {
                    Stream.of(
                            dto.getGroupId(),
                            dto.getProductId(),
                            dto.getStatus()
                    ).forEach(Objects::requireNonNull);
                    return dto;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }

    public static Mono<GroupSKUParameterDTO> validateBody(Mono<GroupSKUParameterDTO> groups) {
        return groups.map(dto -> {
            dto.getData().stream()
                    .filter(Objects::nonNull)
                    .forEach(o -> {
                        if (o.getGroupId() == null) {
                            throw new IllegalArgumentException("id no puede ser nulo.");
                        }
                        if (o.getStatus() == null) {
                            throw new IllegalArgumentException("status no puede ser nulo.");
                        }
                        if (o.getProductId() == null) {
                            throw new IllegalArgumentException("productId no puede ser nulo.");
                        }
                    });
            return dto;
        }).onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }

    private ValidationsGroup(){}
}
