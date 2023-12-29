package cl.camanchaca.domain.generic;

import lombok.Getter;

import java.util.Optional;

@Getter
public abstract class ErrorGeneric extends Exception {
    private final String message;
    private Optional<String> type;

    public ErrorGeneric(String message, String type) {
        this.message = message;
        this.type = Optional.ofNullable(type);
    }


}
