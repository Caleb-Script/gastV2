package com.acme.gast.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/**
 * Aufzählung für das Geschlecht. Dies kann beispielsweise verwendet werden,
 * um Optionsfelder auf der Clientseite zu implementieren.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 */
public enum GeschlechtTyp {
    /**
     * Männlich mit dem internen Wert 'M',
     * verwendet für die Zuordnung in einem JSON-Datensatz oder zur Speicherung in einer Datenbank.
     */
    MAENNLICH("M"),
    /**
     * Weiblich mit dem internen Wert 'W',
     * verwendet für die Zuordnung in einem JSON-Datensatz oder zur Speicherung in einer Datenbank.
     */
    WEIBLICH("W"),
    /**
     * Divers mit dem internen Wert 'D',
     * verwendet für die Zuordnung in einem JSON-Datensatz oder zur Speicherung in einer Datenbank.
     */
    DIVERS("D");

    private final String typ;

    GeschlechtTyp(final String value) {
        this.typ = value;
    }

    /**
     * Konvertiere einen String in einen Enum-Wert.
     *
     * @param value Der String, für den ein entsprechender Enum-Wert ermittelt werden soll.
     * @return Passender Enum-Wert oder null.
     */
    @JsonCreator
    public static GeschlechtTyp of(final String value) {
        return Stream.of(values())
                .filter(gender -> gender.typ.equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gib einen Enum-Wert als String mit dem internen Wert aus.
     * Dieser Wert wird von Jackson in einem JSON-Datensatz verwendet.
     * [<a href="https://github.com/FasterXML/jackson-databind/wiki">Wiki-Seiten</a>]
     *
     * @return Interner Wert
     */
    @JsonValue
    public String getTyp() {
        return typ;
    }
}
