package cl.camanchaca.optimization.mapper;

import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import cl.camanchaca.optimization.adapter.postgres.demand.UnrestrictedDemandData;

public class UnrestrictedDemandMapper {
    public static UnrestrictedDemand toUnrestrictedDemand(UnrestrictedDemandData data) {
        return UnrestrictedDemand.builder()
                .unrestrictedDemandId(data.getUnrestrictedDemandId())
                .productsId(data.getProductsId())
                .especie(data.getEspecie())
                .nombreSector(data.getNombreSector())
                .paisDestino(data.getPaisDestino())
                .destinatario(data.getDestinatario())
                .tipoContrato(data.getTipoContrato())
                .dv(data.getDv())
                .contrato(data.getContrato())
                .calidad(data.getCalidad())
                .puertoDestino(data.getPuertoDestino())
                .codigo(data.getCodigo())
                .forma(data.getForma())
                .calibre(data.getCalibre())
                .familia(data.getFamilia())
                .oficina(data.getOficina())
                .kilosNetos(data.getKilosNetos())
                .fob(data.getFob())
                .rmp(data.getRmp())
                .auxOp(data.getAuxOp())
                .rend(data.getRend())
                .kgWFEDemanda(data.getKgwfeDemanda())
                .kgWFEPlan(data.getKgwfePlan())
                .dif(data.getDif())
                .periodo(data.getPeriodo())
                .precioCierreUSDKgNeto(data.getPrecioCierreUSDKgNeto())
                .flete(data.getFlete())
                .libras(data.getLibras())
                .estatus(data.getEstatus())
                .periodoCarga(data.getPeriodoCarga())
                .orderType(data.getOrderType())
                .customerProgram(data.getCustomerProgram())
                .officeSalesType(data.getOfficeSalesType())
                .family(data.getFamily())
                .extRmp(data.getExtRmp())
                .trimDlbWK(data.getTrimDlbWK())
                .estado(data.getEstado())
                .usuario(data.getUsuario())
                .build();
    }

    public static UnrestrictedDemandData toUnrestrictedDemandData(UnrestrictedDemand data) {
        return UnrestrictedDemandData.builder()
                .unrestrictedDemandId(data.getUnrestrictedDemandId())
                .productsId(data.getProductsId())
                .especie(data.getEspecie())
                .nombreSector(data.getNombreSector())
                .paisDestino(data.getPaisDestino())
                .destinatario(data.getDestinatario())
                .tipoContrato(data.getTipoContrato())
                .dv(data.getDv())
                .contrato(data.getContrato())
                .calidad(data.getCalidad())
                .puertoDestino(data.getPuertoDestino())
                .codigo(data.getCodigo())
                .forma(data.getForma())
                .calibre(data.getCalibre())
                .familia(data.getFamilia())
                .oficina(data.getOficina())
                .kilosNetos(data.getKilosNetos())
                .fob(data.getFob())
                .rmp(data.getRmp())
                .auxOp(data.getAuxOp())
                .rend(data.getRend())
                .kgwfeDemanda(data.getKgWFEDemanda())
                .kgwfePlan(data.getKgWFEPlan())
                .dif(data.getDif())
                .periodo(data.getPeriodo())
                .precioCierreUSDKgNeto(data.getPrecioCierreUSDKgNeto())
                .flete(data.getFlete())
                .libras(data.getLibras())
                .estatus(data.getEstatus())
                .periodoCarga(data.getPeriodoCarga())
                .orderType(data.getOrderType())
                .customerProgram(data.getCustomerProgram())
                .officeSalesType(data.getOfficeSalesType())
                .family(data.getFamily())
                .extRmp(data.getExtRmp())
                .trimDlbWK(data.getTrimDlbWK())
                .estado(data.getEstado())
                .usuario(data.getUsuario())
                .build();
    }


}
