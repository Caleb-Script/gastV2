
package com.acme.gast.mail;

import com.acme.gast.MailProps;
import com.acme.gast.entity.Gast;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static jakarta.mail.Message.RecipientType.TO;

/**
 * Mail-Client.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ClassNamePrefixedWithPackageName")
public class Mailer {
    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private static final boolean SMTP_ACTIVATED = Objects.equals(System.getenv("SMTP_ACTIVATED"), "true") ||
        Objects.equals(System.getProperty("smtp-activated"), "true");

    private final JavaMailSender mailSender;
    private final MailProps props;

    @Value("${spring.mail.host}")
    private String mailhost;

    /**
     * Email senden, dass es einen neuen Gaeste gibt.
     *
     * @param neuerKunde Das Objekt des neuen Gaeste.
     */
    @Async
    @SuppressWarnings({"CatchParameterName", "IllegalIdentifierName", "LocalFinalVariableName"})
    public void send(final Gast neuerKunde) {
        if (!SMTP_ACTIVATED) {
            log.warn("SMTP ist deaktiviert.");
        }
        final MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setFrom(new InternetAddress(props.from()));
            mimeMessage.setRecipient(TO, new InternetAddress(props.sales()));
            mimeMessage.setSubject("Neuer Gast " + neuerKunde.getId());
            final var body = "<strong>Neuer Gast:</strong> <em>" + neuerKunde.getNachname() + "</em>";
            log.trace("send: Mailserver={}, Thread-ID={}, body={}", mailhost, Thread.currentThread().threadId(), body);
            mimeMessage.setText(body);
            mimeMessage.setHeader("Content-Type", "text/html");
        };

        try {
            mailSender.send(preparator);
        } catch (final MailSendException | MailAuthenticationException _) {
            // TODO Wiederholung, um die Email zu senden
            log.warn("Email nicht gesendet: Ist der Mailserver {} erreichbar?", mailhost);
        }
    }
}
