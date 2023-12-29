package cl.camanchaca.domain.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    Integer orderId;
    BigDecimal docVent;
    String line;
    Integer code;
    String name;
    String size;
    String client;
    String country;
    String emp;
    BigDecimal rmp;
    BigDecimal kilo;
    BigDecimal ordered;
    String familyType;
    LocalDate deliveryDate;
    String plant;
    String sectorName;
    String sectorNum;
    String specie;
    String incoTerms;
    String planificationStatus;
    BigDecimal kilosNeto;
    BigDecimal wfeCantOrderKg;
    BigDecimal fobRef;
    String destinationPortDelivery;
    String calidad;
    String forma;
    String nombrePaisDestinoPedido;
    String nombreDestinatarioMercanciaPedido;
    BigDecimal cierrePedido;
    String umPrecioContrato;
    String umDocumento;
    BigDecimal cierrePedidoUmb;
    BigDecimal rendimiento;
    LocalDateTime updateDate;
}
