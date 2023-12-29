package cl.camanchaca.biomass.validations;

import cl.camanchaca.domain.dtos.biomass.SizeBiomassDTO;
import cl.camanchaca.generics.errors.InfrastructureError;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class ValidationSizeBiomass {
    public static Mono<SizeBiomassDTO> validateSizeBiomass(Mono<SizeBiomassDTO> sizeBiomassDTOMono) {
        return sizeBiomassDTOMono.map(data -> {
                    data.getData().stream()
                            .forEach(size -> {
                                Stream.of(
                                        size.getUnit(),
                                        size.getPieceType(),
                                        size.getInitialRange(),
                                        size.getFinalRange(),
                                        size.getSpecies()
                                ).forEach(Objects::requireNonNull);
                            });
                    return data;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }

    private ValidationSizeBiomass(){}
}
