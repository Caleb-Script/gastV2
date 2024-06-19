
package com.acme.gast.controller;

import com.acme.gast.service.AccessForbiddenException;
import com.acme.gast.service.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Handler für allgemeine Exceptions.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@ControllerAdvice
@Slf4j
class CommonExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    void onNotFound(final NotFoundException ex) {
        log.debug("onNotFound: {}", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(FORBIDDEN)
    void onAccessForbidden(final AccessForbiddenException ex) {
        log.debug("onAccessForbidden: {}", ex.getMessage());
    }
}
