package com.acme.gast.controller;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Gaeste.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 * @param betrag Betrag
 * @param waehrung Währung
 */
record BuchungDTO(
    BigDecimal betrag,

    @Positive(message = "Der Betrag muss größer als 0 sein")
    Currency waehrung
) {
}
