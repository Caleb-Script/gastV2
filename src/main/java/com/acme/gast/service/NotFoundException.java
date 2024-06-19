
package com.acme.gast.service;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * RuntimeException, falls kein Gast gefunden wurde.
 */
@Getter
public final class NotFoundException extends RuntimeException {
    /**
     * Nicht-vorhandene ID.
     */
    private final UUID id;

    /**
     * Suchkriterien, zu denen nichts gefunden wurde.
     */
    private final Map<String, List<String>> suchkriterien;

    NotFoundException(final UUID id) {
        super("Kein Gast mit der ID " + id + " gefunden.");
        this.id = id;
        suchkriterien = null;
    }

    NotFoundException(final Map<String, List<String>> suchkriterien) {
        super("Keine Gaeste gefunden.");
        id = null;
        this.suchkriterien = suchkriterien;
    }

    NotFoundException() {
        super("Keine Gaeste gefunden.");
        id = null;
        suchkriterien = null;
    }
}
