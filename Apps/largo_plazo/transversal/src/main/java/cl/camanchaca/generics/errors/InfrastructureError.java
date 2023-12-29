package cl.camanchaca.generics.errors;
import cl.camanchaca.domain.generic.ErrorGeneric;

public class InfrastructureError extends ErrorGeneric {
    public InfrastructureError(String code) {
        super(findErrorMessage(code), "Infrastructure");
    }


    private static String findErrorMessage(String code){
        return ErrorsEnum.getByCode(code).getMessage();
    }

}
