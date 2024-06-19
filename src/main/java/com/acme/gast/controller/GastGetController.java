
package com.acme.gast.controller;

import com.acme.gast.entity.Gast;
import com.acme.gast.security.JwtService;
import com.acme.gast.service.GastReadService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.acme.gast.controller.GastGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden. Public, damit Pfade für Zugriffsschutz verwendet werden können.
 * <img src="../../../../../asciidoc/GastGetController.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@RestController
@RequestMapping(REST_PATH)
@OpenAPIDefinition(info = @Info(title = "Gast API", version = "v2"))
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
public class GastGetController {
    /**
     * Basispfad für die REST-Schnittstelle.
     */
    public static final String REST_PATH = "/rest";

    /**
     * Pfad, um Nachnamen abzufragen.
     */
    public static final String NACHNAME_PATH = "/nachname";

    /**
     * Muster für eine UUID.
     */
    public static final String ID_PATTERN = "[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}";

    private final GastReadService service;
    private final JwtService jwtService;
    private final UriHelper uriHelper;

    // https://localhost:8080/swagger-ui.html
    // https://localhost:8080/swagger-ui.html
    /**
     * Suche anhand der Gast-ID als Pfad-Parameter.
     *
     * @param id ID des zu suchenden Gaeste
     * @param version Versionsnummer aus dem Header If-None-Match
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @param jwt JWT für Security
     * @return Ein Response mit dem Statuscode 200 und dem gefundenen Gaeste mit Atom-Links oder Statuscode 404.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    // "Distributed Tracing" durch https://micrometer.io bei Aufruf eines anderen Microservice
    @Observed(name = "get-by-id")
    @Operation(summary = "Suche mit der Gast-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Gast gefunden")
    @ApiResponse(responseCode = "404", description = "Gast nicht gefunden")
    @SuppressWarnings("ReturnCount")
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    ResponseEntity<GastModel> getById(
        @PathVariable final UUID id,
        @RequestHeader("If-None-Match") final Optional<String> version,
        final HttpServletRequest request,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        final var username = jwtService.getUsername(jwt);
        log.debug("getById: id={}, version={}, username={}", id, version, username);
        // KEIN Optional https://github.com/spring-projects/spring-security/issues/3208
        if (username == null) {
            log.error("Trotz Spring Security wurde getById() ohne Benutzername im JWT aufgerufen");
            return status(UNAUTHORIZED).build();
        }
        final var rollen = jwtService.getRollen(jwt);
        log.trace("getById: rollen={}", rollen);

        final var gast = service.findById(id, username, rollen, false);
        log.trace("getById: {}", gast);

        final var currentVersion = "\"" + gast.getVersion() + '"';
        if (Objects.equals(version.orElse(null), currentVersion)) {
            return status(NOT_MODIFIED).build();
        }

        final var model = gastToModel(gast, request);
        log.debug("getById: model={}", model);
        return ok().eTag(currentVersion).body(model);
    }

    private GastModel gastToModel(final Gast gast, final HttpServletRequest request) {
        final var model = new GastModel(gast);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + gast.getId();

        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);
        return model;
    }

    /**
     * Suche mit diversen Suchkriterien als Query-Parameter.
     *
     * @param suchkriterien Query-Parameter als Map.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Ein Response mit dem Statuscode 200 und den gefundenen Gaeste als CollectionModel oder Statuscode 404.
     */
    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mid den Gaeste")
    @ApiResponse(responseCode = "404", description = "Keine Gaeste gefunden")
    CollectionModel<GastModel> get(
        @RequestParam @NonNull final MultiValueMap<String, String> suchkriterien,
        final HttpServletRequest request
    ) {
        log.debug("get: suchkriterien={}", suchkriterien);

        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var models = service.find(suchkriterien)
            .stream()
            .map(gast -> {
                final var model = new GastModel(gast);
                model.add(Link.of(baseUri + '/' + gast.getId()));
                return model;
            })
            .toList();
        log.debug("get: {}", models);
        return CollectionModel.of(models);
    }

    /**
     * Abfrage, welche Nachnamen es zu einem Präfix gibt.
     *
     * @param prefix Nachname-Präfix als Pfadvariable.
     * @return Die passenden Nachnamen oder Statuscode 404, falls es keine gibt.
     */
    @GetMapping(path = NACHNAME_PATH + "/{prefix}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche Nachnamen mit Praefix", tags = "Suchen")
    String getNachnamenByPrefix(@PathVariable final String prefix) {
        log.debug("getNachnamenByPrefix: {}", prefix);
        final var nachnamen = service.findNachnamenByPrefix(prefix);
        log.debug("getNachnamenByPrefix: {}", nachnamen);
        return nachnamen.stream()
            .map(nachname -> "\"" + nachname + '"')
            .toList()
            .toString();
    }
}
