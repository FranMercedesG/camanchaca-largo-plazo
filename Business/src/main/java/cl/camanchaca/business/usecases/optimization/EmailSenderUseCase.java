package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.repositories.mailsender.JavaMailSenderRepository;
import cl.camanchaca.domain.models.optimization.EmailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@Slf4j
@AllArgsConstructor
public class EmailSenderUseCase {

    private final JavaMailSenderRepository mailSender;

    public Mono<Void> apply(InputStream fileToSend, EmailSender emailSender) throws Exception {
        return mailSender.sendEmail(fileToSend, emailSender)
                .doOnSuccess(o -> log.info("Email enviado"))
                .doOnError(e -> log.error("Error en envio email: " + e.getMessage()));
    }
}

