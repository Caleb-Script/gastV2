package com.acme.gast.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/**
 * Enum für Zimmertypen. Dies kann beispielsweise verwendet werden, um Optionen auf der Clientseite zu implementieren.
 */
public enum ZimmerTyp {
    /**
     * Einzelzimmer mit dem internen Wert 'E'.
     */
    EINZELZIMMER("E"),
    /**
     * Doppelzimmer mit dem internen Wert 'D'.
     */
    DOPPELZIMMER("D"),
    /**
     * Suite mit dem internen Wert 'S'.
     */
    SUITE("S");

    private final String typ;

    ZimmerTyp(final String typ) {
        this.typ = typ;
    }

    /**
     * Konvertiert einen String in einen Enum-Wert.
     *
     * @param value Der String, für den ein entsprechender Enum-Wert ermittelt werden soll.
     * @return Der passende Enum-Wert oder null.
     */
    @JsonCreator
    public static ZimmerTyp of(final String value) {
        return Stream.of(values())
                .filter(zimmerTyp -> zimmerTyp.typ.equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gibt den Enum-Wert als String mit dem internen Wert aus.
     * Dieser Wert wird von Jackson in einem JSON-Datensatz verwendet.
     *
     * @return Der interne Wert
     */
    @JsonValue
    public String getTyp() {
        return typ;
    }
}
