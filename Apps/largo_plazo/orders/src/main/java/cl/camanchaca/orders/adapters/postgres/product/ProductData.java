package cl.camanchaca.orders.adapters.postgres.product;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@Table("products")
public class ProductData {

    @Column("anomes")
    private Integer anomes;

    @Id
    @Column("codigo")
    private Integer codigo;

    @Column("ajuste_sp")
    private BigDecimal ajusteSp;

    @Column("ajuste_sp_pr")
    private BigDecimal ajusteSpPr;

    @Column("bloques_bp")
    private BigDecimal bloquesBp;

    @Column("bloques_harasu")
    private BigDecimal bloquesHarasu;

    @Column("c_dist")
    private BigDecimal cDist;

    @Column("c_emp")
    private BigDecimal cEmp;

    @Column("c_proc_total")
    private BigDecimal cProcTotal;

    @Column("caja")
    private String caja;

    @Column("calibre")
    private String calibre;

    @Column("calibre_trimd")
    private String calibreTrimd;

    @Column("calidad")
    private String calidad;

    @Column("calidad_last")
    private String calidadLast;

    @Column("calidad_obj")
    private String calidadObj;

    @Column("color")
    private String color;

    @Column("degr_wfe")
    private BigDecimal degrWfe;

    @Column("descripcion")
    private String descripcion;

    @Column("emp_primario")
    private String empPrimario;

    @Column("estado")
    private String estado;

    @Column("especie")
    private String especie;

    @Column("factor_sp")
    private BigDecimal factorSp;

    @Column("familia")
    private String familia;

    @Column("forma")
    private String forma;

    @Column("harina")
    private BigDecimal harina;

    @Column("mandt")
    private Integer mandt;

    @Column("mesano")
    private Integer mesano;

    @Column("neto_wfe")
    private BigDecimal netoWfe;

    @Column("rendimiento")
    private BigDecimal rendimiento;

    @Column("scale")
    private String scale;

    @Column("scrape_meat")
    private BigDecimal scrapeMeat;

    @Column("skin")
    private String skin;

    @Column("sobrepeso")
    private BigDecimal sobrepeso;

    @Column("subp_wfe")
    private BigDecimal subpWfe;

    @Column("target")
    private String target;

    @Column("tipo")
    private String tipo;

    @Column("ult_per_prod")
    private String ultPerProd;

    @Column("wfe_bk_bp")
    private BigDecimal wfeBkBp;

    @Column("wfe_bk_harasu")
    private BigDecimal wfeBkHarasu;

    @Column("wfe_harina")
    private BigDecimal wfeHarina;

    @Column("wfe_meat")
    private BigDecimal wfeMeat;

}

