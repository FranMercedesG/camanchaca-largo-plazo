package cl.camanchaca.orders.validations;

import cl.camanchaca.utils.LocalDateUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public class ValidationOrder {

  public static Mono<LocalDate> validateHeaderUCAndParamsDate(
    ServerRequest request
  ) {
    return GenericValidations
      .validateHeader(request, "UC")
      .flatMap(header -> GenericValidations.validateQueryParam(request, "date"))
      .map(LocalDateUtils::parseStringToDate);
  }

  private ValidationOrder() {}
}
