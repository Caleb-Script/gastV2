package com.acme.gast.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/**
 * Enum-Typ der Zimmerpräferenz typen für den Gast.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 */
public enum PraeferenzTyp {
    /**
     * Nichtraucher mit dem internen Wert 'N',
     * verwendet für die Zuordnung in einem JSON-Datensatz oder zur Speicherung in einer Datenbank.
     */
    NICHTRAUCHER("N"),
    /**
     * Meerblick mit dem internen Wert 'M',
     * verwendet für die Zuordnung in einem JSON-Datensatz oder zur Speicherung in einer Datenbank.
     */
    MEERBLICK("M"),
    /**
     * Bergblick mit dem internen Wert 'BB'.
     */
    BERGBLICK("BB"),
    /**
     * Balkon mit dem internen Wert 'BK'.
     */
    BALKON("BK");

    private final String typ;

    PraeferenzTyp(final String value) {
        this.typ = value;
    }

    /**
     * Konvertiert einen String in einen Enum-Wert.
     *
     * @param value Der String, für den ein entsprechender Enum-Wert ermittelt werden soll.
     * @return Passender Enum-Wert oder null.
     */
    @JsonCreator
    public static PraeferenzTyp of(final String value) {
        return Stream.of(values())
                .filter(praeferenz -> praeferenz.typ.equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gibt den Enum-Wert als String mit dem internen Wert aus.
     * Dieser Wert wird von Jackson in einem JSON-Datensatz verwendet.
     *
     * @return Interner Wert
     */
    @JsonValue
    public String getTyp() {
        return typ;
    }
}
