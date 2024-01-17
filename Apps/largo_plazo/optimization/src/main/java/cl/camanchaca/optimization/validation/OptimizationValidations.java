package cl.camanchaca.optimization.validation;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.domain.models.optimization.EmailSender;
import cl.camanchaca.generics.errors.InfraestructureException;
import cl.camanchaca.generics.errors.InfrastructureError;
import cl.camanchaca.utils.LocalDateUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class OptimizationValidations {


    public static Mono<LocalDate> validateMonth(ServerRequest request) {
      return   Mono.fromCallable(() -> request.queryParam("month")
                        .get())
                .map(LocalDateUtils::parseStringToDate)
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }
    public static Mono<Integer> validateVersion(ServerRequest request) {
        return Mono.fromCallable(() -> request.queryParam("version")
                        .get())
                .map(Integer::parseInt)
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
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

    public static Mono<RequestParams> validateRequestParams(ServerRequest request, RequestParams params) {
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

    public static Mono<EmailSender> validateRequestParams( Mono<Map<String, String>> r) {
        return r.map(request -> {
            if (!request.containsKey("toEmail")) {
                throw new IllegalArgumentException("Required parameter missing:: toEmail");
            }

            String toEmail = request.get("toEmail");

            if (!request.containsKey("subject")) {
                throw new IllegalArgumentException("Required parameter missing:: subject");
            }

            String subject = request.get("subject");

            if (!request.containsKey("text")) {
                throw new IllegalArgumentException("Required parameter missing:: text");
            }

            String text = request.get("text");

            return EmailSender
                    .builder()
                    .to(toEmail)
                    .subject(subject)
                    .text(text)
                    .build();
        }).onErrorResume(throwable ->
                Mono.error(new InfrastructureError("02")));
    }


    private OptimizationValidations(){}
}
