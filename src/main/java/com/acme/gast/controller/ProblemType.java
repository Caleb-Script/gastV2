
package com.acme.gast.controller;

import lombok.Getter;

/**
 * Enum für ProblemDetail.type.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Getter
public enum ProblemType {
    /**
     * Constraints als Fehlerursache.
     */
    CONSTRAINTS("constraints"),

    /**
     * Fehler, wenn z.B. Emailadresse bereits existiert.
     */
    UNPROCESSABLE("unprocessable"),

    /**
     * Fehler beim Header If-Match.
     */
    PRECONDITION("precondition"),

    /**
     * Fehler bei z.B. einer Patch-Operation.
     */
    BAD_REQUEST("badRequest");

    private final String value;

    ProblemType(final String value) {
        this.value = value;
    }
}
