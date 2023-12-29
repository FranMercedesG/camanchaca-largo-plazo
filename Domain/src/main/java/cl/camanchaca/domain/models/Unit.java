package cl.camanchaca.domain.models;

public enum Unit {
    LB, OZ, KG, G, CM;

    public static Unit fromString(String value) {
        for (Unit unit : Unit.values()) {
            if (unit.name().equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("No matching enum value for: " + value);
    }
}
