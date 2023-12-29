package cl.camanchaca.orders.utils;

public enum SalesStatusEnum {
    CONTRATO("Contrato"),
    SPOT("Spot"),
    MINIMUM_REQUEST("Minimum Request");

    private final String value;

    SalesStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

