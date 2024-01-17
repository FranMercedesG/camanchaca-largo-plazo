package cl.camanchaca.domain.generic;

import lombok.Getter;


@Getter
public abstract class ErrorGeneric extends Exception {
    private final String message;
    private final String type;

    protected ErrorGeneric(String message, String type) {
        this.message = message;
        this.type = type;
    }


}
