package cl.camanchaca.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Product {
        private Integer anomes;
        private Integer codigo;
        private BigDecimal ajusteSp;
        private BigDecimal ajusteSpPr;
        private BigDecimal bloquesBp;
        private BigDecimal bloquesHarasu;
        private BigDecimal cDist;
        private BigDecimal cEmp;
        private BigDecimal cProcTotal;
        private String caja;
        private String calibre;
        private String calibreTrimd;
        private String calidad;
        private String calidadLast;
        private String calidadObj;
        private String color;
        private BigDecimal degrWfe;
        private String descripcion;
        private String empPrimario;
        private String estado;
        private String especie;
        private BigDecimal factorSp;
        private String familia;
        private String forma;
        private BigDecimal harina;
        private Integer mandt;
        private Integer mesano;
        private BigDecimal netoWfe;
        private BigDecimal rendimiento;
        private String scale;
        private BigDecimal scrapeMeat;
        private String skin;
        private BigDecimal sobrepeso;
        private BigDecimal subpWfe;
        private String target;
        private String tipo;
        private String ultPerProd;
        private BigDecimal wfeBkBp;
        private BigDecimal wfeBkHarasu;
        private BigDecimal wfeHarina;
        private BigDecimal wfeMeat;
}
