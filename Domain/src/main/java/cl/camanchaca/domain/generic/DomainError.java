package cl.camanchaca.domain.generic;

public class DomainError extends ErrorGeneric{
    public DomainError(String message) {
        super(message, "Domain");
    }

}
