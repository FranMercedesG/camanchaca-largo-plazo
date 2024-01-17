package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.demand.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class SummaryOfficeUseCase {

    private final UnrestrictedDemandRepository unrestrictedDemandRepository;

    private final PeriodRepository periodRepository;
    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue())).collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period period = periods.get(0);

                    var offset = (requestParams.getPage() - 1) * requestParams.getSize();
                    
                    return unrestrictedDemandRepository.getAllOfficeBetweenDatesAndPageAndSizeAndFilters(
                                    offset, requestParams.getSize(), period.getInitialPeriod(), period.getFinalPeriod(),requestParams.getSpecie(), requestParams.getFamily(),  header.get(Constans.OFFICE.getValue())
                            )
                            .collectList()
                            .flatMap(unrestrictedDemandList ->
                                    Flux.fromIterable(unrestrictedDemandList)
                                            .groupBy(demand ->
                                                    Arrays.asList(
                                                            demand.getCustomerProgram(),
                                                            demand.getEspecie(),
                                                            demand.getOfficeSalesType(),
                                                            demand.getOrderType()
                                                    )
                                            )
                                            .flatMap(group ->
                                                    group.collectList()
                                                            .map(demands -> {

                                                                List<PrecioCierreUsdKgNetoDetail> kilosNetos = demands.stream()
                                                                        .map(demand -> new PrecioCierreUsdKgNetoDetail(demand.getPeriodo(), demand.getPrecioCierreUSDKgNeto()))
                                                                        .collect(Collectors.toList());

                                                                List<LibrasProdFwDetail> librasProdDetail = demands.stream()
                                                                        .map(demand -> new LibrasProdFwDetail(demand.getPeriodo(), demand.getLibras()))
                                                                        .collect(Collectors.toList());

                                                                List<FobDetail> fobDetails = demands.stream()
                                                                        .map(demand -> new FobDetail(demand.getPeriodo(), demand.getFob()))
                                                                        .collect(Collectors.toList());

                                                                List<RmpDetail> rmpDetails = demands.stream()
                                                                        .map(demand -> new RmpDetail(demand.getPeriodo(), demand.getRmp()))
                                                                        .collect(Collectors.toList());

                                                                UnrestrictedDemandOfficeSummary  uo = UnrestrictedDemandOfficeSummary.builder()
                                                                        .especie(demands.get(0).getEspecie())
                                                                        .nombreSector(demands.get(0).getNombreSector())
                                                                        .paisDestino(demands.get(0).getPaisDestino())
                                                                        .destinatario(demands.get(0).getDestinatario())
                                                                        .tipoContrato(demands.get(0).getTipoContrato())
                                                                        .dv(demands.get(0).getDv())
                                                                        .contrato(demands.get(0).getContrato())
                                                                        .calidad(demands.get(0).getCalidad())
                                                                        .puertoDestino(demands.get(0).getPuertoDestino())
                                                                        .codigo(demands.get(0).getCodigo())
                                                                        .forma(demands.get(0).getForma())
                                                                        .calibre(demands.get(0).getCalibre())
                                                                        .familia(demands.get(0).getFamilia())
                                                                        .oficina(demands.get(0).getOficina())
                                                                        .auxOp(demands.get(0).getAuxOp())
                                                                        .rend(demands.get(0).getRend())
                                                                        .kgWFEPlan(demands.get(0).getKgWFEPlan())
                                                                        .dif(demands.get(0).getDif())
                                                                        .periodo(demands.get(0).getPeriodo())
                                                                        .purchPrice(demands.get(0).getPrecioCierreUSDKgNeto())
                                                                        .flete(demands.get(0).getFlete())
                                                                        .prodFw(demands.get(0).getLibras())
                                                                        .estatus(demands.get(0).getEstatus())
                                                                        .periodoCarga(demands.get(0).getPeriodoCarga())
                                                                        .orderType(demands.get(0).getOrderType())
                                                                        .customerProgram(demands.get(0).getCustomerProgram())
                                                                        .officeSalesType(demands.get(0).getOfficeSalesType())
                                                                        .family(demands.get(0).getFamily())
                                                                        .extRmp(demands.get(0).getExtRmp())
                                                                        .trimDlbWK(demands.get(0).getTrimDlbWK())
                                                                        .estado(demands.get(0).getEstado())
                                                                        .usuario(demands.get(0).getUsuario())
                                                                        .build();

                                                                uo.setPurchPrice(sumTotal(kilosNetos.stream().map(PrecioCierreUsdKgNetoDetail::getValue).collect(Collectors.toList())));
                                                                uo.setRmp(sumTotal(rmpDetails.stream().map(RmpDetail::getValue).collect(Collectors.toList())));
                                                                uo.setFob(sumTotal(fobDetails.stream().map(FobDetail::getValue).collect(Collectors.toList())));
                                                                uo.setProdFw(sumTotal(librasProdDetail.stream().map(LibrasProdFwDetail::getValue).collect(Collectors.toList())));
                                                                return uo;
                                                            })
                                            )
                                            .collectList()
                            )
                            .map(o -> {

                                long totalCount = o.size();

                                if(requestParams.getSpecie().isBlank()){
                                    return ParametersResponse.of(o, totalCount);
                                }

                                return ParametersResponse.of(o.stream().filter(e -> e.getEspecie().equalsIgnoreCase(requestParams.getSpecie())).collect(Collectors.toList()),
                                       Long.valueOf(o.stream().filter(e -> e.getEspecie().equalsIgnoreCase(requestParams.getSpecie())).collect(Collectors.toList()).size()));
                            });
                });
    }

    private  <T extends Number> BigDecimal sumTotal(List<T> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return numbers.stream()
                .map(number -> BigDecimal.valueOf(number.doubleValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
