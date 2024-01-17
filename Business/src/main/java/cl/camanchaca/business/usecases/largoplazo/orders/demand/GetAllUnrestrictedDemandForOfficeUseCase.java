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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class GetAllUnrestrictedDemandForOfficeUseCase {

    private final UnrestrictedDemandRepository unrestrictedDemandRepository;

    private final PeriodRepository periodRepository;

    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue())).collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period period = periods.get(0);

                    return unrestrictedDemandRepository.getAllOfficeBetweenDatesAndFilters(
                            period.getInitialPeriod(), period.getFinalPeriod(),requestParams.getSpecie(), requestParams.getFamily(),  header.get(Constans.OFFICE.getValue())
                            )
                            .collectList()
                            .flatMap(unrestrictedDemandList -> {

                                int numberOfPages = getTotalPage(requestParams.getSize(), unrestrictedDemandList.size()) - 1;

                                return Flux.fromIterable(unrestrictedDemandList)
                                        .groupBy(demand ->
                                                Arrays.asList(
                                                        demand.getOficina(),
                                                        demand.getOfficeSalesType(),
                                                        demand.getPaisDestino(),
                                                        demand.getProductsId(),
                                                        demand.getCustomerProgram(),
                                                        demand.getEspecie(),
                                                        demand.getOrderType(),
                                                        demand.getSize(),
                                                        demand.getIncoterm(),
                                                        demand.getPuertoDestino()
                                                )
                                        )
                                        .flatMap(group ->
                                                group.collectList()
                                                        .map(demands -> {
                                                            List<PrecioCierreUsdKgNetoDetail> precioCierreUsdKgNetoDetails = demands.stream()
                                                                    .map(demand -> new PrecioCierreUsdKgNetoDetail(demand.getPeriodo(), demand.getPrecioCierreUSDKgNeto()))
                                                                    .sorted(Comparator.comparing(PrecioCierreUsdKgNetoDetail::getDate))
                                                                    .collect(Collectors.toList());

                                                            List<LibrasProdFwDetail> librasProdFwDetails = demands.stream()
                                                                    .map(demand -> new LibrasProdFwDetail(demand.getPeriodo(), demand.getLibras()))
                                                                    .sorted(Comparator.comparing(LibrasProdFwDetail::getDate))
                                                                    .collect(Collectors.toList());

                                                            List<FobDetail> fobDetails = demands.stream()
                                                                    .map(demand -> new FobDetail(demand.getPeriodo(), demand.getFob()))
                                                                    .sorted(Comparator.comparing(FobDetail::getDate))
                                                                    .collect(Collectors.toList());

                                                            List<RmpDetail> rmpDetails = demands.stream()
                                                                    .map(demand -> new RmpDetail(demand.getPeriodo(), demand.getRmp()))
                                                                    .sorted(Comparator.comparing(RmpDetail::getDate))
                                                                    .collect(Collectors.toList());

                                                            List<KgWFEDemandaDetail> KgWFEDemandaDetails = demands.stream()
                                                                    .map(demand -> new KgWFEDemandaDetail(demand.getPeriodo(), demand.getKgWFEDemanda()))
                                                                    .sorted(Comparator.comparing(KgWFEDemandaDetail::getDate))
                                                                    .collect(Collectors.toList());

                                                            List<KilosNetosDetail> kilosNetosDetails = demands.stream()
                                                                    .map(demand -> new KilosNetosDetail(demand.getPeriodo(), demand.getKilosNetos()))
                                                                    .sorted(Comparator.comparing(KilosNetosDetail::getDate))
                                                                    .collect(Collectors.toList());


                                                            UnrestrictedDemandOffice uo = UnrestrictedDemandOffice.builder()
                                                                    .oficina(demands.get(0).getOficina())
                                                                    .officeSalesType(demands.get(0).getOfficeSalesType())
                                                                    .customerProgram(demands.get(0).getCustomerProgram())
                                                                    .paisDestino(demands.get(0).getPaisDestino())
                                                                    .estatus(demands.get(0).getEstatus())
                                                                    .productsId(demands.get(0).getProductsId())
                                                                    .orderType(demands.get(0).getOrderType())
                                                                    .forma(demands.get(0).getForma())
                                                                    .calidad(demands.get(0).getCalidad())
                                                                    .calibre(demands.get(0).getCalibre())
                                                                    .rend(demands.get(0).getRend())
                                                                    .puertoDestino(demands.get(0).getPuertoDestino())
                                                                    .auxOp(demands.get(0).getAuxOp())
                                                                    .kgWFEPlan(demands.get(0).getKgWFEPlan())
                                                                    .dif(demands.get(0).getDif())
                                                                    .periodo(demands.get(0).getPeriodo())
                                                                    .flete(demands.get(0).getFlete())
                                                                    .periodoCarga(demands.get(0).getPeriodoCarga())
                                                                    .family(demands.get(0).getFamily())
                                                                    .extRmp(demands.get(0).getExtRmp())
                                                                    .trimDlbWK(demands.get(0).getTrimDlbWK())
                                                                    .estado(demands.get(0).getEstado())
                                                                    .usuario(demands.get(0).getUsuario())
                                                                    .especie(demands.get(0).getEspecie())
                                                                    .familia(demands.get(0).getFamilia())
                                                                    .nombreSector(demands.get(0).getNombreSector())
                                                                    .destinatario(demands.get(0).getDestinatario())
                                                                    .tipoContrato(demands.get(0).getTipoContrato())
                                                                    .dv(demands.get(0).getDv())
                                                                    .contrato(demands.get(0).getContrato())
                                                                    .codigo(demands.get(0).getCodigo())
                                                                    .incoterm(demands.get(0).getIncoterm())
                                                                    .seguro(demands.get(0).getSeguro())
                                                                    .descripcion(demands.get(0).getDescripcion())
                                                                    .size(demands.get(0).getSize())
                                                                    .build();
                                                            uo.setPurchPrice(precioCierreUsdKgNetoDetails);
                                                            uo.setProdFw(librasProdFwDetails);
                                                            uo.setRmp(rmpDetails);
                                                            uo.setFob(fobDetails);
                                                            uo.setKgWFEDemanda(KgWFEDemandaDetails);
                                                            uo.setKilosNetos(kilosNetosDetails);
                                                            return uo;
                                                        })
                                        )
                                        .collectList()
                                        .flatMapMany(unrestrictedDemandOffices -> {
                                            
                                            int start = (requestParams.getPage() - 1) * requestParams.getSize();

                                            if (start >= unrestrictedDemandOffices.size()) {
                                                return Flux.empty();
                                            }

                                            int end = Math.min(start + requestParams.getSize(), unrestrictedDemandOffices.size());

                                            return Flux.fromIterable(unrestrictedDemandOffices.subList(start, end));
                                        })
                                        .collectList()
                                        .map(unrestrictedDemandOffices -> ParametersResponse.of(unrestrictedDemandOffices, (long) numberOfPages));
                            });

                });
    }

    private int getTotalPage(int pageSize, int totalElements) {
        if (totalElements == 0) {
            return 0;
        }
        return (totalElements - 1) / pageSize + 1;
    }


}
