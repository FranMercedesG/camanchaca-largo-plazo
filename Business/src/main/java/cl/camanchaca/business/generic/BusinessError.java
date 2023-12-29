package cl.camanchaca.business.generic;

import cl.camanchaca.domain.generic.ErrorGeneric;

public class BusinessError extends ErrorGeneric {
    public BusinessError(String message) {
        super(message, "Business");
    }

}
