package cl.camanchaca.biomass.validations;

import cl.camanchaca.domain.dtos.biomass.AvailableBiomassDTO;
import cl.camanchaca.generics.errors.InfrastructureError;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ValidationAvailableBiomass {
    public static Mono<AvailableBiomassDTO> validateAvailableBiomass(Mono<AvailableBiomassDTO> availableBiomassFlux) {
        return availableBiomassFlux.map(data -> {
                    data.getData().stream()
                            .map(availableBiomass -> {
                                if (Boolean.FALSE.equals(validateDateFormat(availableBiomass.getFecpro()))) {
                                    throw new RuntimeException("Formato FecPro errado");
                                }
                                return availableBiomass;
                            })
                            .forEach(availableBiomass ->
                                Stream.of(
                                        availableBiomass.getFecpro(),
                                        availableBiomass.getHu(),
                                        availableBiomass.getExternalHu(),
                                        availableBiomass.getType(),
                                        availableBiomass.getSize(),
                                        availableBiomass.getQuality(),
                                        availableBiomass.getHeader(),
                                        availableBiomass.getManufacturingOrder(),
                                        availableBiomass.getBatch(),
                                        availableBiomass.getFolio(),
                                        availableBiomass.getCenter(),
                                        availableBiomass.getPermanency(),
                                        availableBiomass.getPieces(),
                                        availableBiomass.getWeight(),
                                        availableBiomass.getSpecie(),
                                        data.getDeletes()
                                ).forEach(Objects::requireNonNull)
                            );

                    return data;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }


    public static boolean validateDateFormat(String str) {
        String regex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    private ValidationAvailableBiomass() {
    }
}
