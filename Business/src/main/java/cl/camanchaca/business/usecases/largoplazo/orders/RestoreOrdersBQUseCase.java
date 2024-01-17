package cl.camanchaca.business.usecases.largoplazo.orders;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.bigquery.SalmonOrdersBQRepository;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.GetAutoCalculateKgWFEDemandUseCase;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.GetAutocalculateFreightUseCase;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.GetAutocalculateInsuranceUseCase;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.SaveUnrestrictedDemandAdminUseCase;
import cl.camanchaca.domain.dtos.GenerateKgWFEDemandRequestDto;
import cl.camanchaca.domain.dtos.GenerateKgWFEDemandResponseDto;
import cl.camanchaca.domain.dtos.InsuranceRequestDto;
import cl.camanchaca.domain.models.Order;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.demand.KilosNetosDetail;
import cl.camanchaca.domain.models.demand.PrecioCierreUsdKgNetoDetail;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class RestoreOrdersBQUseCase {
    public static final String BIGQUERY = "bigquery";
    private final PeriodRepository periodRepository;
    private final SaveUnrestrictedDemandAdminUseCase saveUnrestrictedDemandAdminUseCase;
    private final SalmonOrdersBQRepository salmonOrdersRepository;
    private final ProductRepository productRepository;
    private final GetAutocalculateFreightUseCase getFreightUseCase;
    private final GetAutocalculateInsuranceUseCase getAutocalculateInsuranceUseCase;
    private final GetAutoCalculateKgWFEDemandUseCase getAutoCalculateKgWFEDemandUseCase;

    public void restoreDataOfBQ() {
        log.info("Iniciar restauracion de bigquery en ordenes");

        Map<String, String> header = Map.of(Constans.USER.getValue(), "system", Constans.OFFICE.getValue(), "chile");

        //TODO change user to Admin
        periodRepository.getSelectedPeriodByUser("user1")
                .collectList()
                .flatMapMany(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.empty();
                    }
                    log.info("lectura de periodos");

                    Period period = periods.get(0);

                    return salmonOrdersRepository.getOrdersBetweenDate(
                            period.getInitialPeriod(), period.getFinalPeriod());
                })
                .flatMap(order -> {
                    log.info("inicia calculo");
                    return productRepository.findById(order.getCode())
                            .flatMap(product -> {

                                BigDecimal kgfwe = getAutoCalculateKgWFEDemandUseCase.calculateKGWFE(order.getRendimiento(), order.getKilosNeto());

                                BigDecimal insurance = getAutocalculateInsuranceUseCase.calculateInsurance(getInsuraceRequest(order));

                                    return Mono.just(
                                            UnrestrictedDemand.builder()
                                                    .oficina(order.getOficina())
                                                    .descripcion(product.getDescripcion())
                                                    .incoterm(order.getIncoTerms())
                                                    .seguro(insurance)
                                                    .officeSalesType("")
                                                    .customerProgram("")
                                                    .paisDestino(order.getCountry())
                                                    .estatus("")
                                                    .especie(order.getSpecie())
                                                    .productsId(order.getCode())
                                                    .orderType(product.getTipo())
                                                    .forma(product.getForma())
                                                    .calidad(product.getCalidad())
                                                    .calibre(order.getSize())
                                                    .rend(order.getRendimiento())
                                                    .puertoDestino(order.getDestinationPortDelivery())
                                                    .kilosNetos(order.getKilosNeto())
                                                    .precioCierreUSDKgNeto(order.getCierrePedido())
                                                    .libras(order.getKilosNeto())
                                                    .fob(order.getFobRef())
                                                    .rmp(order.getRmp())
                                                    .auxOp("")
                                                    .kgWFEDemanda(kgfwe)
                                                    .kgWFEPlan(kgfwe)
                                                    .dif("")
                                                    .periodo(order.getDeliveryDate())
                                                    .flete(BigDecimal.ZERO) //NO ESTA, se llama desde bigquery
                                                    .periodoCarga(order.getDeliveryDate())
                                                    .family(product.getFamilia())
                                                    .extRmp("")
                                                    .trimDlbWK(BigDecimal.ZERO) //NO ESTA
                                                    .size(order.getEmp())
                                                    .estado(order.getPlanificationStatus())
                                                    .destinatario(order.getNombreDestinatarioMercanciaPedido())
                                                    .dv(order.getOrderId().toString())
                                            .build());
                            });
                })
                .collectList()
                .flatMapMany(demands -> {
                    log.info("finaliza calculo");
                    if (demands.isEmpty()) {
                        return Mono.empty();
                    }
                    log.info("inicia guardado de demandas");
                    return saveUnrestrictedDemandAdminUseCase.apply(demands, header, BIGQUERY);
                })
                .subscribe(
                        null,
                        error -> log.error("Hubo un error al cargar los datos de bigquery: ", error),
                        () -> log.info("Completada restauracion de ordenes")
                );


    }

    private static GenerateKgWFEDemandRequestDto getGenerateKgWFEDemandRequestDto(Order order) {
        return GenerateKgWFEDemandRequestDto
                .builder()
                .rend(order.getRendimiento())
                .kilosNetos(List.of(KilosNetosDetail
                        .builder()
                        .date(null)
                        .value(order.getKilosNeto())
                        .build())
                ).build();
    }

    private static InsuranceRequestDto getInsuraceRequest(Order order) {
        return InsuranceRequestDto
                .builder()
                .incoterm(order.getIncoTerms())
                .unit(order.getEmp())
                .purchPrice(List.of(PrecioCierreUsdKgNetoDetail.builder().value(order.getCierrePedido()).date(null).build()))
                .build();
    }


}
