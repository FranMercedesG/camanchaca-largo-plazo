package cl.camanchaca.business.generic;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class RequestParams {
    private Integer page;
    private Integer size;
    private String office;
    private String specie;
    private String family;
    private UUID groupId;
    private UUID plantId;
    private String selectedScenario;
    private LocalDateTime from;
    private LocalDateTime to;
    private LocalDate date;
    private String toEmail;
    private String subject;
    private String text;
}
