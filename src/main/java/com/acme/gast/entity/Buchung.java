package com.acme.gast.entity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Diese Klasse repräsentiert eine Buchung.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 */
@Entity
@Table(name = "buchung")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Buchung {
    @Id
    @GeneratedValue
    private UUID id;
    /**
     * Die Währung der Bezahlung.
     */
    private Currency waehrung;

    /**
     * Der Betrag der Buchung.
     */
    private BigDecimal betrag;
}

