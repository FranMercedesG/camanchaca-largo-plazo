package cl.camanchaca.generics.errors;

import lombok.Getter;

import java.util.Optional;

@Getter
public class ErrorResponse {

    private final String message;
    private Optional<String> type;

    public ErrorResponse(String message, String type) {
        this.message = message;
        this.type = Optional.ofNullable(type);
    }

    public ErrorResponse(String message) {
        this.message = message;
        this.type = Optional.ofNullable("Unknow");
    }
}
