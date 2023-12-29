package cl.camanchaca.biomass.validations;

import cl.camanchaca.domain.models.biomass.RealBiomass;
import cl.camanchaca.generics.errors.InfrastructureError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class ValidationRealBiomass {
    public static Flux<RealBiomass> validateRealBiomass(Flux<RealBiomass> products) {
        return products.map(product -> {
                    Stream.of(
                    ).forEach(Objects::requireNonNull);
                    return product;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }

    private ValidationRealBiomass(){}
}
