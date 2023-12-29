package cl.camanchaca.biomass.validations;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.responses.GroupSizeMaxResponse;
import cl.camanchaca.domain.dtos.biomass.AvailableBiomassDTO;
import cl.camanchaca.generics.errors.InfrastructureError;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BiomassValidations {
    private BiomassValidations() {

    }

    public static Mono<RequestParams> validateParamsPagination(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam("pageSize").get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new RuntimeException("La pagina o el tamaño no puede ser menor a 0");
                    }
                    return params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .build();
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static Mono<RequestParams> validateParams(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam("pageSize").get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new RuntimeException("La pagina o el tamaño no puede ser menor a 0");
                    }

                    UUID groupId = UUID.fromString(request.queryParam("groupId").get());

                    if (groupId == null) {
                        throw new RuntimeException("groupId no puede ser null");
                    }

                    String specie = request.queryParam("specie").get();

                    if (specie == null) {
                        throw new RuntimeException("specie no puede ser null");
                    }

                    return params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .groupId(groupId)
                            .specie(specie)
                            .build();
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static void validateHeader(ServerRequest.Headers headers) {
        Stream.of(
                headers.header("user").get(0),
                headers.header("office").get(0)
        ).forEach(Objects::requireNonNull);
    }

    public static Mono<GroupSizeMaxResponse> validateSaveGroupMax(Mono<GroupSizeMaxResponse> availableBiomassFlux) {
        return availableBiomassFlux.map(data -> {
                    data.getData().stream()
                            .forEach(availableBiomass ->
                                    Stream.of(
                                            availableBiomass.getGroupId(),
                                            availableBiomass.getPeriod(),
                                            availableBiomass.getMaxLimit()
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
}
