package cl.camanchaca.optimization.adapter.postgres.demand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "unrestricted_demand")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnrestrictedDemandData {
    @Id
    @Column("unrestricted_demand_id")
    private UUID unrestrictedDemandId;

    @Column("products_id")
    private Integer productsId;

    @Column("especie")
    private String especie;

    @Column("nombre_sector")
    private String nombreSector;

    @Column("pais_destino")
    private String paisDestino;

    @Column("destinatario")
    private String destinatario;

    @Column("tipo_contrato")
    private String tipoContrato;

    @Column("dv")
    private String dv;

    @Column("contrato")
    private String contrato;

    @Column("calidad")
    private String calidad;

    @Column("puerto_destino")
    private String puertoDestino;

    @Column("codigo")
    private String codigo;

    @Column("forma")
    private String forma;

    @Column("calibre")
    private String calibre;

    @Column("familia")
    private String familia;

    @Column("oficina")
    private String oficina;

    @Column("kilos_netos")
    private BigDecimal kilosNetos;

    @Column("fob")
    private BigDecimal fob;

    @Column("rmp")
    private BigDecimal rmp;

    @Column("aux_op")
    private String auxOp;

    @Column("rend")
    private BigDecimal rend;

    @Column("kgwfe_demanda")
    private BigDecimal kgwfeDemanda;

    @Column("kgwfe_plan")
    private BigDecimal kgwfePlan;

    @Column("dif")
    private String dif;

    @Column("periodo")
    private LocalDate periodo;

    @Column("precio_cierre_usd_kgneto")
    private BigDecimal precioCierreUSDKgNeto;

    @Column("flete")
    private BigDecimal flete;

    @Column("libras")
    private BigDecimal libras;

    @Column("estatus")
    private String estatus;

    @Column("periodo_carga")
    private LocalDate periodoCarga;

    @Column("order_type")
    private String orderType;

    @Column("customer_program")
    private String customerProgram;

    @Column("office_sales_type")
    private String officeSalesType;

    @Column("family")
    private String family;

    @Column("ext_rmp")
    private String extRmp;

    @Column("trim_dlbwk")
    private BigDecimal trimDlbWK;

    @Column("estado")
    private String estado;

    @Column("usuario")
    private String usuario;
}
