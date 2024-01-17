package cl.camanchaca.biomass.validations;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.responses.GroupSizeMaxResponse;
import cl.camanchaca.generics.errors.InfraestructureException;
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
                .<RequestParams>handle((page, sink) -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam(Constans.PAGE_SIZE.getValue()).get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        sink.error(new InfraestructureException(Constans.PAGE_ERROR.getValue()));
                        return;
                    }
                    sink.next(params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .build());
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static Mono<RequestParams> validateParams(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam(Constans.PAGE_SIZE.getValue()).get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new InfraestructureException(Constans.PAGE_ERROR.getValue());
                    }

                    UUID groupId = UUID.fromString(request.queryParam("groupId").get());

                    if (groupId == null) {
                        throw new InfraestructureException("groupId no puede ser null");
                    }

                    String specie = request.queryParam(Constans.SPECIE.getValue()).get();

                    if (specie == null) {
                        throw new InfraestructureException("specie no puede ser null");
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
                headers.header(Constans.USER.getValue()).get(0),
                headers.header(Constans.OFFICE.getValue()).get(0)
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
