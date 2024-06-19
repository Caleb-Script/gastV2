package com.acme.gast.controller;

import com.acme.gast.entity.ZimmerTyp;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * DTO für das Neuanlegen und Ändern eines neuen Gaeste.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 * @param zimmerNummer Die Zimmernummer des Zimmers.
 * @param zimmerTyp Der Typ des Zimmers.
 */
@Builder
record ZimmerInformationDTO(
    @NotNull(message = "Die Zimmernummer fehlt")
    @Pattern(regexp = ZIMMER_NUMMER_PATTERN, message = "Ungültige Zimmernummer")
    String zimmerNummer,

    @NotNull(message = "Der Zimmertyp fehlt")
    ZimmerTyp zimmerTyp
) {
    /**
     * Muster für eine gültige Zimmernummer (z.B. 306, 207, 106, 007).
     */
    public static final String ZIMMER_NUMMER_PATTERN = "\\d{3}";
}
