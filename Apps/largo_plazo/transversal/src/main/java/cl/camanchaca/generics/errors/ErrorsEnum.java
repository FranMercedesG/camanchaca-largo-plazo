package cl.camanchaca.generics.errors;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorsEnum {

    GENERIC_ERROR("00", "Unknow error"),
    ERROR_HEADER("01", "Error in headers"),
    ERROR_QUERYPARAMS("02", "Error in queryParams");

    private final String code;
    private final String message;

    ErrorsEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorsEnum getByCode(String code){
        return Arrays.stream(ErrorsEnum.values())
                .filter(errorsEnum -> errorsEnum.getCode().equals(code))
                .findFirst()
                .orElse(GENERIC_ERROR);
    }
}
