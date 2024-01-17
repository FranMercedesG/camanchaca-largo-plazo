package cl.camanchaca.business.repositories.mailsender;

import cl.camanchaca.domain.models.optimization.EmailSender;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface JavaMailSenderRepository {
    Mono<Void> sendEmail(InputStream fileToSend, EmailSender emailSender) throws Exception;
}
