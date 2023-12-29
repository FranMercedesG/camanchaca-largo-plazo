package cl.camanchaca.domain.models.demand;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnrestrictedDemandOfficeExcel {
    private String oficina;
    private String officeSalesType;
    private String customerProgram;
    private String paisDestino;
    private String estatus;
    private String codigo;
    private String destinatario;
    private String especie;
    private String familia;
    private String orderType;
    private String forma;
    private String calidad;
    private String calibre;
    private BigDecimal rend;
    private String puertoDestino;
    private List<PrecioCierreUsdKgNetoDetail> purchPrice;
    private List<LibrasProdFwDetail> prodFw;
    private List<FobDetail> fob;
    private List<RmpDetail> rmp;
}
