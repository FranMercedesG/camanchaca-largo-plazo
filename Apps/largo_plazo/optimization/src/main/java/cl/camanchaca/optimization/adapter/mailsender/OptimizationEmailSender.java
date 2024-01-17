package cl.camanchaca.optimization.adapter.mailsender;

import cl.camanchaca.business.repositories.mailsender.JavaMailSenderRepository;
import cl.camanchaca.domain.models.optimization.EmailSender;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;

@Service
@RequiredArgsConstructor
public class OptimizationEmailSender implements JavaMailSenderRepository {

    private final JavaMailSender mailSender;
    public Mono<Void> sendEmail(InputStream input, EmailSender mail) {

        return Mono.fromCallable(() -> buildAttachment(input))
                .flatMap(attachment ->
                        Mono.fromCallable(() -> createMessage(mail, attachment))
                                .flatMap(this::sendMessage)
                )
                .onErrorResume(Mono::error);
    }

    private File buildAttachment(InputStream input) throws IOException {

        String ext = getExtensionFromType(input);

        File file = File.createTempFile("attach", "." + ext);

        OutputStream out = new FileOutputStream(file);

        input.transferTo(out);

        out.close();

        return file;
    }

    private MimeMessage createMessage(EmailSender mail, File attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getText());

        helper.addAttachment(attachment.getName(), attachment);

        return message;
    }

    private void addAttachment(MimeMessage msg, File file) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.addAttachment(file.getName(), file);
    }


    private String getExtensionFromType(InputStream input) throws IOException {

        Tika tika = new Tika();

        String type = tika.detect(input);

        switch (type) {
            case "application/msword":
                return "doc";

            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return "docx";

            case "application/vnd.ms-excel":
                return "xls";

            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return "xlsx";

            case "application/pdf":
                return "pdf";

            case "text/plain":
                return "txt";

            case "image/jpeg":
                return "jpg";

            case "image/png":
                return "png";

            case "application/zip":
                return "zip";

            default:
                return "xlsx";

        }

    }

    private Mono<Void> sendMessage(MimeMessage msg) {
        return Mono.fromRunnable(() ->
                mailSender.send(msg)
        ).then();
    }




}
