package com.acme.gast.entity;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import static com.acme.gast.entity.Gast.ZIMMER_INFO_BUCHUNGEN_GRAPH;
import static com.acme.gast.entity.Gast.ZIMMER_INFO_GRAPH;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static java.util.Collections.emptyList;

/**
 * Diese Klasse repräsentiert einen Hotelgast mit
 * verschiedenen Attributen wie Name, E-Mail, VIP-Status, Treuestufe,
 * Geburtsdatum, Geschlecht, Präferenzen, Zimmerinformationen,
 * Buchungshistorie sowie Erstellung- und Aktualisierungszeitstempel.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 */
@Entity
@Table(name = "gast")
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = ZIMMER_INFO_GRAPH, attributeNodes = @NamedAttributeNode("zimmerInfo"))
@NamedEntityGraph(name = ZIMMER_INFO_BUCHUNGEN_GRAPH, attributeNodes = {
    @NamedAttributeNode("zimmerInfo"), @NamedAttributeNode("buchungen")
})
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
public class Gast {
    /**
     * NamedEntityGraph für das Attribut "zimmerInfo".
     */
    public static final String ZIMMER_INFO_GRAPH = "Gast.zimmerInfo";

    /**
     * NamedEntityGraph für die Attribute "zimmerInfo" und "buchungen".
     */
    public static final String ZIMMER_INFO_BUCHUNGEN_GRAPH = "Gast.zimmerInfoBuchungGen";

    /**
     * Die ID vom Gast als UUID.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Versionsnummer für optimistische Synchronisation.
     */
    @Version
    private int version;

    /**
     * Der Nachname des Gasts.
     */
    private String nachname;

    /**
     * Der Vorname des Gasts.
     */
    private String vorname;

    /**
     * Die E-Mail-ZimmerInformation des Gasts.
     */
    private String email;

    /**
     * Gibt an, ob der Gast ein VIP ist.
     */
    private boolean istVip;

    /**
     * Stufe oder Status des Gastes im Treueprogramm des Hotels.
     */
    private int rang;

    /**
     * Das Geburtsdatum des Gasts.
     */
    private LocalDate geburtsdatum;

    /**
     * URL zum Portfolio des Gasts.
     */
    private URL homepage;

    /**
     * Geschlecht des Gasts.
     */
    @Enumerated(STRING)
    private GeschlechtTyp geschlecht;

    /**
     * Die Zimmerinformation des Gasts.
     */
    @OneToOne(optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY, orphanRemoval = true)
    @ToString.Exclude
    private ZimmerInformation zimmerInfo;

    /**
     * Buchungshistorie des Gasts.
     */
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "gast_id")
    @OrderColumn(name = "idx", nullable = false)
    @ToString.Exclude
    private List<Buchung> buchungen;

    /**
     * Die Liste der präferenzen des Gasts.
     */
    @Transient
    private List<PraeferenzTyp> praeferenzen;

    @Column(name = "praeferenzen")
    private String praeferenzenStr;

    private String username;

    @CreationTimestamp
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    private LocalDateTime aktualisiert;

    /**
     * Gaestedaten überschreiben.
     *
     * @param gast Neue Gaestedaten.
     */
    public void set(final Gast gast) {
        nachname = gast.nachname;
        vorname = gast.vorname;
        email = gast.email;
        istVip = gast.istVip;
        rang = gast.rang;
        geschlecht = gast.geschlecht;
        geburtsdatum = gast.geburtsdatum;
        homepage = gast.homepage;
    }

    @PrePersist
    private void buildInteressenStr() {
        if (praeferenzen == null || praeferenzen.isEmpty()) {
            // NULL in der DB-Spalte
            praeferenzenStr = null;
            return;
        }
        final var stringList = praeferenzen.stream()
            .map(Enum::name)
            .toList();
        praeferenzenStr = String.join(",", stringList);
    }

    @PostLoad
    @SuppressWarnings("java:S6204")
    private void loadInteressen() {
        if (praeferenzenStr == null) {
            // NULL in der DB-Spalte
            praeferenzen = emptyList();
            return;
        }
        final var praeferenzenArray = praeferenzenStr.split(",");
        praeferenzen = Arrays.stream(praeferenzenArray)
            .map(PraeferenzTyp::valueOf)
            .collect(Collectors.toList());
    }
}
