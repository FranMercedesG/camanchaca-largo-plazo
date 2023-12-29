package cl.camanchaca.domain.models.demand;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnrestrictedDemand {

    private UUID unrestrictedDemandId;
    private String oficina;
    private String officeSalesType;
    private String customerProgram;
    private String paisDestino;
    private String estatus;
    private Integer productsId;
    private String incoterm;
    private String descripcion;
    private BigDecimal seguro;
    private String size;  //UNIT

    private String orderType;
    private String forma;
    private String calidad;
    private String calibre;
    private BigDecimal rend;
    private String puertoDestino;

    private BigDecimal kilosNetos;
    private BigDecimal precioCierreUSDKgNeto;
    private BigDecimal libras;
    private BigDecimal fob;
    private BigDecimal rmp;
    private String auxOp;
    private BigDecimal kgWFEDemanda;
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
    private String origen;
    private LocalDateTime updatedAt;

}
