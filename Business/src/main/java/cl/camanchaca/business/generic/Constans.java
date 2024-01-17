package cl.camanchaca.business.generic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Constans {
    OFFICE("office"),
    USER("user"),
    PAGE_SIZE("pageSize"),
    PAGE_ERROR("La pagina o el tamaño no puede ser menor a 0"),
    NO_IMPLEMENT_METHOD("Metodo no implementado"),
    SPECIE("specie"),
    ;
    private final String value;
}
