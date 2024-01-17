package cl.camanchaca.domain.models.optimization;

import lombok.*;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSender {
    private String to;
    private String subject;
    private String text;
}
