
package com.acme.gast.service;

import com.acme.gast.entity.Gast;
import com.acme.gast.repository.GastRepository;
import com.acme.gast.repository.SpecificationBuilder;
import com.acme.gast.security.Rolle;
import io.micrometer.observation.annotation.Observed;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.acme.gast.security.Rolle.ADMIN;

/**
 * Anwendungslogik für Gaeste.
 * <img src="../../../../../asciidoc/GastReadService.svg" alt="Klassendiagramm">
 * Schreiboperationen werden mit Transaktionen durchgeführt und Lese-Operationen mit Readonly-Transaktionen:
 * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#transactions">siehe Dokumentation</a>.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#transactions
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GastReadService {
    private final GastRepository gastRepository;
    private final SpecificationBuilder specificationBuilder;

    /**
     * Einen Gaeste anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Gaeste
     * @param username Benutzername aus einem JWT
     * @param rollen Rollen als Liste von Enums
     * @param fetchUmsaetze true, falls die Umsätze mitgeladen werden sollen
     * @return Der gefundene Gast
     * @throws NotFoundException Falls kein Gast gefunden wurde
     * @throws AccessForbiddenException Falls die erforderlichen Rollen nicht gegeben sind
     */
    @Observed(name = "find-by-id")
    public @NonNull Gast findById(
        final UUID id,
        final String username,
        final List<Rolle> rollen,
        final boolean fetchUmsaetze
    ) {
        log.debug("findById: id={}, username={}, rollen={}", id, username, rollen);

        final var kundeOptional = fetchUmsaetze ? gastRepository.findByIdFetchBuchungen(id) : gastRepository.findById(id);
        final var gast = kundeOptional.orElse(null);
        log.trace("findById: gast={}", gast);

        // beide find()-Methoden liefern ein Optional
        if (gast != null && gast.getUsername().contentEquals(username)) {
            // eigene Gaestedaten
            return gast;
        }

        if (!rollen.contains(ADMIN)) {
            // nicht admin, aber keine eigenen (oder keine) Gaestedaten
            throw new AccessForbiddenException(rollen);
        }

        // admin: Gaestedaten evtl. nicht gefunden
        if (gast == null) {
            throw new NotFoundException(id);
        }
        log.debug("findById: gast={}, umsaetze={}", gast, fetchUmsaetze ? gast.getBuchungen() : "N/A");
        return gast;
    }

    /**
     * Gaeste anhand von Suchkriterien als Collection suchen.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Gaeste oder eine leere Liste
     * @throws NotFoundException Falls keine Gaeste gefunden wurden
     */
    @SuppressWarnings("ReturnCount")
    public @NonNull Collection<Gast> find(@NonNull final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return gastRepository.findAll();
        }

        if (suchkriterien.size() == 1) {
            final var nachnamen = suchkriterien.get("nachname");
            if (nachnamen != null && nachnamen.size() == 1) {
                return findByNachname(nachnamen.getFirst(), suchkriterien);
            }

            final var emails = suchkriterien.get("email");
            if (emails != null && emails.size() == 1) {
                return findByEmail(emails.getFirst(), suchkriterien);
            }
        }

        final var specification = specificationBuilder
            .build(suchkriterien)
            .orElseThrow(() -> new NotFoundException(suchkriterien));
        final var gaeste = gastRepository.findAll(specification);
        if (gaeste.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", gaeste);
        return gaeste;
    }

    private List<Gast> findByNachname(final String nachname, final Map<String, List<String>> suchkriterien) {
        log.trace("findByNachname: {}", nachname);
        final var gaeste = gastRepository.findByNachname(nachname);
        if (gaeste.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("findByNachname: {}", gaeste);
        return gaeste;
    }

    private Collection<Gast> findByEmail(final String email, final Map<String, List<String>> suchkriterien) {
        log.trace("findByEmail: {}", email);
        final var gast = gastRepository
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException(suchkriterien));
        final var gaeste = List.of(gast);
        log.debug("findByEmail: {}", gaeste);
        return gaeste;
    }

    /**
     * Abfrage, welche Nachnamen es zu einem Präfix gibt.
     *
     * @param prefix Nachname-Präfix.
     * @return Die passenden Nachnamen in alphabetischer Reihenfolge.
     * @throws NotFoundException Falls keine Nachnamen gefunden wurden.
     */
    public @NonNull List<String> findNachnamenByPrefix(final String prefix) {
        log.debug("findNachnamenByPrefix: {}", prefix);
        final var nachnamen = gastRepository.findNachnamenByPrefix(prefix);
        if (nachnamen.isEmpty()) {
            //noinspection NewExceptionWithoutArguments
            throw new NotFoundException();
        }
        log.debug("findNachnamenByPrefix: {}", nachnamen);
        return nachnamen;
    }
}
