package com.acme.gast.controller;

import com.acme.gast.entity.GeschlechtTyp;
import com.acme.gast.entity.PraeferenzTyp;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import org.hibernate.validator.constraints.UniqueElements;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Gaeste. Beim Lesen wird die Klasse GastModel für die Ausgabe
 * verwendet.Die Klasse enthält auch Validierungen für die Eingabefelder,
 * um sicherzustellen, dass gültige Daten eingegeben werden.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 * @param nachname     Gültiger Nachname eines Gaeste, d.h. mit einem geeigneten Muster.
 * @param vorname      Vorname eines Gaeste.
 * @param email        Email eines Gaeste.
 * @param istVip       Flag, das angibt, ob der Gast VIP ist.
 * @param rang         Rang des Gaeste.
 * @param geburtsdatum Das Geburtsdatum eines Gaeste.
 * @param homepage     Die Homepage eines Gaeste.
 * @param geschlecht   Das Geschlecht eines Gaeste.
 * @param praeferenzen Die Präferenzen eines Gaeste.
 * @param zimmerInfo   Informationen zum Zimmer des Gaeste.
 * @param buchungen    Die Buchungen des Gaeste.
 */
@Builder
@SuppressWarnings("RecordComponentNumber")
record GastDTO(
    @NotNull(message = "Der Nachname fehlt")
    @Pattern(regexp = NACHNAME_PATTERN)
    @Size(max = NAME_MAX_LAENGE)
    String nachname,

    @NotNull(message = "Der Vornamen fehlt")
    @Pattern(regexp = VORNAME_PATTERN)
    @Size(max = NAME_MAX_LAENGE)
    String vorname,

    @Email
    @NotNull(message = "Die Email fehlt")
    @Size(max = EMAIL_MAX_LAENGE)
    String email,

    boolean istVip,

    @Max(value = MAX_RANG, message = "Der Rang darf maximal {value} sein")
    @Min(value = MIN_RANG, message = "Der Rang darf nicht kleiner als {value} sein")
    int rang,

    @Past
    @NotNull
    LocalDate geburtsdatum,

    @Column(length = HOMEPAGE_MAX_LAENGE)
    URL homepage,

    @NotNull(message = "Kein Geschlecht angegeben")
    GeschlechtTyp geschlecht,

    @Valid
    @NotNull(groups = OnCreate.class)
    ZimmerInformationDTO zimmerInfo,

    List<BuchungDTO> buchungen,

    @UniqueElements(message = "{value} darf nicht doppelt vorkommen")
    List<PraeferenzTyp> praeferenzen,

    String username,
    String password
) {
    /**
     * Marker-Interface f&uuml;r Jakarta Validation: zus&auml;tzliche Validierung
     * beim Neuanlegen.
     */
    interface OnCreate {
    }

    /**
     * Muster für einen gültigen Nachnamen.
     */
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Muster für einen gültigen Vornamen.
     */
    public static final String VORNAME_PATTERN =
        "[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Minimaler Wert für eine Kategorie.
     */
    public static final long MIN_RANG = 0L;

    /**
     * Maximaler Wert für eine Kategorie.
     */
    public static final long MAX_RANG = 9L;
    /**
     * Maximale Länge für den Nachnamen, Vornamen und die Homepage.
     */
    private static final int NAME_MAX_LAENGE = 40;
    private static final int EMAIL_MAX_LAENGE = 40;
    private static final int HOMEPAGE_MAX_LAENGE = 40;
}
