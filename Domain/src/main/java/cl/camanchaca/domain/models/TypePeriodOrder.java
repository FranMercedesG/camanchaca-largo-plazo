package cl.camanchaca.domain.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TypePeriodOrder {
    
    private LocalDate initialDate;
    private LocalDate finalDate;
    private LocalDate day;
    private TypePeriodOrderEnum type;

}
