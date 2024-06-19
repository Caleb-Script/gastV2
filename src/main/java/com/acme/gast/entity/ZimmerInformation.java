package com.acme.gast.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Diese Klasse repräsentiert Informationen zu einem Zimmer.
 */
@Entity
@Table(name = "zimmer_information")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ZimmerInformation {
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Die Zimmernummer.
     */
    private String zimmerNummer;

    /**
     * Der Zimmertyp.
     */
    @Enumerated(EnumType.STRING)
    private ZimmerTyp zimmerTyp;

}
