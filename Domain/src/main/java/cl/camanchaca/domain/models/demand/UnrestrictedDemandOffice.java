package cl.camanchaca.domain.models.demand;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnrestrictedDemandOffice {
    private UUID unrestrictedDemandId;
    private String oficina;
    private String officeSalesType;
    private String customerProgram;
    private String paisDestino;
    private String estatus;
    private Integer productsId;

    private String orderType;
    private String forma;
    private String calidad;
    private String calibre;
    private BigDecimal rend;
    private String puertoDestino;

    private List<KilosNetosDetail> kilosNetos;
    private List<PrecioCierreUsdKgNetoDetail> purchPrice;
    private List<LibrasProdFwDetail> prodFw;
    private List<FobDetail> fob;
    private List<RmpDetail> rmp;

    private String auxOp;
    private List<kgWFEDemandaDetail> kgWFEDemanda;
    private BigDecimal kgWFEPlan;
    private String dif;
    private LocalDate periodo;
    private BigDecimal flete;
    private LocalDate periodoCarga;
    private String family;
    private String extRmp;
    private BigDecimal trimDlbWK;
    private String estado;
    private String usuario;

    private String especie;
    private String familia;
    private String nombreSector;
    private String destinatario;
    private String tipoContrato;
    private String dv;
    private String contrato;
    private String codigo;
    private String incoterm;
    private BigDecimal seguro;
    private String descripcion;
    private String size;
}
