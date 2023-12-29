package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.DownloadOfficeRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.demand.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class DownloadDemandUnrestrictedOfficeUseCase {

    private final UnrestrictedDemandRepository unrestrictedDemandRepository;

    private final PeriodRepository periodRepository;

    private final DownloadOfficeRepository officeRepository;

    public Mono<byte[]> apply(RequestParams requestParams, Map<String, String> headerInfo) {
        return officeRepository.downloadOfficeFile(getData(requestParams, headerInfo), headerInfo);
    }

    private Flux<UnrestrictedDemandOffice> getData(RequestParams requestParams, Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get("user")).collectList()
                .flatMapMany(periods -> {
                    if (periods.isEmpty()) {
                        return Flux.empty();
                    }

                    Period period = periods.get(0);

                    return unrestrictedDemandRepository.getAllBetweenDatesByOffice(period.getInitialPeriod(), period.getFinalPeriod(), header.get("office")
                            )
                            .collectList()
                            .flatMapMany(unrestrictedDemandList ->
                                    Flux.fromIterable(unrestrictedDemandList)
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

                                                                List<kgWFEDemandaDetail> kgWFEDemandaDetails = demands.stream()
                                                                        .map(demand -> new kgWFEDemandaDetail(demand.getPeriodo(), demand.getKgWFEDemanda()))
                                                                        .sorted(Comparator.comparing(kgWFEDemandaDetail::getDate))
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
                                                                uo.setKgWFEDemanda(kgWFEDemandaDetails);
                                                                uo.setKilosNetos(kilosNetosDetails);
                                                                return uo;
                                                            })
                                            )
                            );
                });
    }

}
