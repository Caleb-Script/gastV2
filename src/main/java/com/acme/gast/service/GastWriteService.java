
package com.acme.gast.service;

import com.acme.gast.entity.Gast;
import com.acme.gast.mail.Mailer;
import com.acme.gast.repository.GastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Anwendungslogik für Gaeste auch mit Bean Validation.
 * <img src="../../../../../asciidoc/GastWriteService.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GastWriteService {
    private final GastRepository gastRepository;
    // private final CustomUserDetailsService userService; // NOSONAR
    private final Mailer mailer;

    /**
     * Einen neuen Gaeste anlegen.
     *
     * @param gast Das Objekt des neu anzulegenden Gaeste.
     * @return Der neu angelegte Gaeste mit generierter ID
     * @throws EmailExistsException Es gibt bereits einen Gaeste mit der Emailadresse.
     */
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#transactions
    @Transactional
    @SuppressWarnings("TrailingComment")
    public Gast create(final Gast gast) {
        log.debug("create: gast={}", gast);
        log.debug("create: adresse={}", gast.getZimmerInfo());
        log.debug("create: umsaetze={}", gast.getBuchungen());

        if (gastRepository.existsByEmail(gast.getEmail())) {
            throw new EmailExistsException(gast.getEmail());
        }

        // TODO Neuen Benutzer im IAM-System anlegen
        // final var login = userService.save(user); // NOSONAR
        gast.setUsername("user");

        final var gastDb = gastRepository.save(gast);

        log.trace("create: Thread-ID={}", Thread.currentThread().threadId());
        mailer.send(gastDb);

        log.debug("create: gastDb={}", gastDb);
        return gastDb;
    }

    /**
     * Einen vorhandenen Gaeste aktualisieren.
     *
     * @param gast Das Objekt mit den neuen Daten (ohne ID)
     * @param id ID des zu aktualisierenden Gaeste
     * @param version Die erforderliche Version
     * @return Aktualisierter Gast mit erhöhter Versionsnummer
     * @throws NotFoundException Kein Gast zur ID vorhanden.
     * @throws VersionOutdatedException Die Versionsnummer ist veraltet und nicht aktuell.
     * @throws EmailExistsException Es gibt bereits einen Gaeste mit der Emailadresse.
     */
    @Transactional
    public Gast update(final Gast gast, final UUID id, final int version) {
        log.debug("update: gast={}", gast);
        log.debug("update: id={}, version={}", id, version);

        var gastDb = gastRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.trace("update: version={}, gastDb={}", version, gastDb);
        if (version != gastDb.getVersion()) {
            throw new VersionOutdatedException(version);
        }

        final var email = gast.getEmail();
        // Ist die neue E-Mail bei einem *ANDEREN* Gaeste vorhanden?
        if (!Objects.equals(email, gastDb.getEmail()) && gastRepository.existsByEmail(email)) {
            log.debug("update: email {} existiert", email);
            throw new EmailExistsException(email);
        }
        log.trace("update: Kein Konflikt mit der Emailadresse");

        // Zu ueberschreibende Werte uebernehmen
        gastDb.set(gast);
        gastDb = gastRepository.save(gastDb);

        log.debug("update: {}", gastDb);
        return gastDb;
    }

    /**
     * Einen Gaeste löschen.
     *
     * @param id Die ID des zu löschenden Gaeste.
     */
    @Transactional
    public void deleteById(final UUID id) {
        log.debug("deleteById: id={}", id);
        final var gast = gastRepository.findById(id).orElse(null);
        if (gast == null) {
            log.debug("deleteById: id={} nicht vorhanden", id);
            return;
        }
        gastRepository.delete(gast);
    }
}
