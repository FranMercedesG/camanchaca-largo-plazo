package cl.camanchaca.orders.mappers.bigquery;

import cl.camanchaca.domain.models.Order;
import cl.camanchaca.utils.LocalDateUtils;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OrderBQMapper {

    public static Order toOrder(FieldValueList row) {
        return Order.builder()
                .orderId(rowToInteger(row.get("PedidoDV")))
                .familyType(rowToString(row.get("FamiliaSTD")))
                .docVent(rowToNulleableBigdecimal(row.get("DocumentoVenta")))
                .code(rowToInteger(row.get("Codigo")))
                .line(rowToString(row.get("PosicionPedido")))
                .name(rowToString(row.get("NombreMaterial")))
                .size(rowToString(row.get("Calibre")))
                .client(rowToString(row.get("NombreSolicitantePedido")))
                .country(rowToString(row.get("PaisSolicitante")))
                .emp(rowToString(row.get("PesoNominal")))
                .rmp(rowToNulleableBigdecimal(row.get("RMPRef")))
                .kilo(rowToNulleableBigdecimal(row.get("CantidadPedidoKG")))
                .ordered(rowToNulleableBigdecimal(row.get("CantidadCajasPedido")))
                .sectorName(rowToString(row.get("NombreSector")))
                .deliveryDate(rowToLocalDate(row.get("FechaPreferenteEntrega")))
                .sectorNum(rowToString(row.get("NumeroSector")))
                .specie(rowToString(row.get("Especie")))
                .incoTerms(rowToString(row.get("Incoterms")))
                .planificationStatus(rowToString(row.get("EstadoPlanif")))
                .kilosNeto(rowToNulleableBigdecimal(row.get("KilosNeto")))
                .wfeCantOrderKg(rowToNulleableBigdecimal(row.get("WFECantPedidoKG")))
                .fobRef(rowToNulleableBigdecimal(row.get("FobRef")))
                .destinationPortDelivery(rowToString(row.get("PuertoDestinoEntrega")))
                .updateDate(rowToLocalDateTime(row.get("FechaActualizacion")))
                .calidad(rowToString(row.get("Calidad")))
                .forma(rowToString(row.get("Forma")))
                .nombrePaisDestinoPedido(rowToString(row.get("NombrePaisDestinoPedido")))
                .nombreDestinatarioMercanciaPedido(rowToString(row.get("NombreDestinatarioMercanciaPedido")))
                .cierrePedido(rowToNulleableBigdecimal(row.get("CierrePedido")))
                .umPrecioContrato(rowToString(row.get("UMPrecioContrato")))
                .umDocumento(rowToString(row.get("UMDocumento")))
                .cierrePedidoUmb(rowToNulleableBigdecimal(row.get("CierrePedidoUMB")))
                .rendimiento(rowToNulleableBigdecimal(row.get("Rendimiento")))
                .build();
    }

    private static BigDecimal rowToNulleableBigdecimal(FieldValue value) {
        return value.isNull()
                ? BigDecimal.ZERO
                : new BigDecimal(value.getStringValue()
                .replace(",", "."));
    }

    private static Integer rowToInteger(FieldValue value) {
        return value.isNull()
                ? null
                : Integer.parseInt(value.getStringValue());

    }

    private static Long rowToLong(FieldValue value) {
        return value.isNull()
                ? null
                : Long.valueOf(value.getStringValue());

    }

    private static String rowToString(FieldValue value) {
        return value.isNull()
                ? null
                : value.getStringValue();

    }

    private static LocalDate rowToLocalDate(FieldValue value) {
        return LocalDateUtils.parseStringToDate(value.getStringValue());
    }

    private static LocalDateTime rowToLocalDateTime(FieldValue value) {
        if (Objects.isNull(value.getStringValue())) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        return LocalDateTime.parse(value.getStringValue(), formatter);
    }

    private OrderBQMapper() {
    }
}
