
package com.acme.gast.controller;

import com.acme.gast.service.EmailExistsException;
import com.acme.gast.service.GastWriteService;
import com.acme.gast.service.VersionOutdatedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.acme.gast.controller.GastGetController.ID_PATTERN;
import static com.acme.gast.controller.GastGetController.REST_PATH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.HttpStatus.PRECONDITION_REQUIRED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 * <img src="../../../../../asciidoc/GastWriteController.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Controller
@RequestMapping(REST_PATH)
@Validated
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "MethodCount", "java:S1075"})
public class GastWriteController {
    /**
     * Basispfad für "type" innerhalb von ProblemDetail.
     */
    @SuppressWarnings("TrailingComment")
    public static final String PROBLEM_PATH = "/problem/";

    private static final String VERSIONSNUMMER_FEHLT = "Versionsnummer fehlt";

    private final GastWriteService service;
    private final GastMapper mapper;
    private final UriHelper uriHelper;

    /**
     * Einen neuen Gast-Datensatz anlegen.
     *
     * @param gastDTO Das Gaesteobjekt mit den Benutzerdaten aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um Location im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422 falls Constraints verletzt
     *      sind oder die Emailadresse bereits existiert oder Statuscode 400 falls syntaktische Fehler im Request-Body
     *      vorliegen.
     * @throws URISyntaxException falls die URI im Request-Objekt nicht korrekt wäre
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen neuen Gaeste anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Gast neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    @SuppressWarnings("TrailingComment")
    ResponseEntity<Void> post(
        @RequestBody @Validated({Default.class, GastDTO.OnCreate.class}) final GastDTO gastDTO,
        final HttpServletRequest request
    ) throws URISyntaxException {
        log.debug("post: gastDTO={}", gastDTO);

        if (gastDTO.username() == null || gastDTO.password() == null) {
            return badRequest().build();
        }

        final var gastInput = mapper.toGast(gastDTO);
        final var gast = service.create(gastInput);
        final var baseUri = uriHelper.getBaseUri(request);
        final var location = new URI(baseUri.toString() + '/' + gast.getId());
        return created(location).build();
    }

    /**
     * Einen vorhandenen Gast-Datensatz überschreiben.
     *
     * @param id ID des zu aktualisierenden Gaeste.
     * @param gastDTO Das Gaesteobjekt aus dem eingegangenen Request-Body.
     * @param version Versionsnummer aus dem Header If-Match
     * @param request Das Request-Objekt, um ggf. die URL für ProblemDetail zu ermitteln
     * @return Response mit Statuscode 204 oder Statuscode 400, falls der JSON-Datensatz syntaktisch nicht korrekt ist
     *      oder 422 falls Constraints verletzt sind oder die Emailadresse bereits existiert
     *      oder 412 falls die Versionsnummer nicht ok ist oder 428 falls die Versionsnummer fehlt.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen Gaeste mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Gast nicht vorhanden")
    @ApiResponse(responseCode = "412", description = "Versionsnummer falsch")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    @ApiResponse(responseCode = "428", description = VERSIONSNUMMER_FEHLT)
    ResponseEntity<Void> put(
        @PathVariable final UUID id,
        @RequestBody @Valid final GastDTO gastDTO,
        @RequestHeader("If-Match") final Optional<String> version,
        final HttpServletRequest request
    ) {
        log.debug("put: id={}, gastDTO={}", id, gastDTO);
        final int versionInt = getVersion(version, request);
        final var gastInput = mapper.toGast(gastDTO);
        final var gast = service.update(gastInput, id, versionInt);
        log.debug("put: {}", gast);
        return noContent().eTag("\"" + gast.getVersion() + '"').build();
    }

    @SuppressWarnings({"MagicNumber", "RedundantSuppression"})
    private int getVersion(final Optional<String> versionOpt, final HttpServletRequest request) {
        log.trace("getVersion: {}", versionOpt);
        final var versionStr = versionOpt.orElseThrow(() -> new VersionInvalidException(
            PRECONDITION_REQUIRED,
            VERSIONSNUMMER_FEHLT,
            URI.create(request.getRequestURL().toString()))
        );
        if (versionStr.length() < 3 ||
            versionStr.charAt(0) != '"' ||
            versionStr.charAt(versionStr.length() - 1) != '"') {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                "Ungueltiges ETag " + versionStr,
                URI.create(request.getRequestURL().toString())
            );
        }

        final int version;
        try {
            version = Integer.parseInt(versionStr.substring(1, versionStr.length() - 1));
        } catch (final NumberFormatException ex) {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                "Ungueltiges ETag " + versionStr,
                URI.create(request.getRequestURL().toString()),
                ex
            );
        }

        log.trace("getVersion: version={}", version);
        return version;
    }



    /**
     * Einen vorhandenen Gaeste anhand seiner ID löschen.
     *
     * @param id ID des zu löschenden Gaeste.
     */
    @DeleteMapping(path = "{id:" + ID_PATTERN + "}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Einen Gaeste anhand der ID loeschen", tags = "Loeschen")
    @ApiResponse(responseCode = "204", description = "Gelöscht")
    void deleteById(@PathVariable final UUID id)  {
        log.debug("deleteById: id={}", id);
        service.deleteById(id);
    }

    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final MethodArgumentNotValidException ex,
        final HttpServletRequest request
    ) {
        log.debug("onConstraintViolations: {}", ex.getMessage());

        final var detailMessages = ex.getDetailMessageArguments();
        final var detail = detailMessages == null
            ? "Constraint Violations"
            : ((String) detailMessages[1]).replace(", and ", ", ");
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onPatchViolations(
        final ConstraintViolationException ex,
        final HttpServletRequest request
    ) {
        final var violations = ex.getConstraintViolations();
        log.debug("onPatchViolations: {}", violations);
        final var msg = violations.stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining(", "));

        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, msg);
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onEmailExists(final EmailExistsException ex, final HttpServletRequest request) {
        log.debug("onEmailExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onVersionOutdated(
        final VersionOutdatedException ex,
        final HttpServletRequest request
    ) {
        log.debug("onVersionOutdated: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(PRECONDITION_FAILED, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.PRECONDITION.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onMessageNotReadable(
        final HttpMessageNotReadableException ex,
        final HttpServletRequest request
    ) {
        log.debug("onMessageNotReadable: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.BAD_REQUEST.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
