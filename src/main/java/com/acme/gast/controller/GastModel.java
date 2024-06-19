package com.acme.gast.controller;

import com.acme.gast.entity.Buchung;
import com.acme.gast.entity.Gast;
import com.acme.gast.entity.GeschlechtTyp;
import com.acme.gast.entity.PraeferenzTyp;
import com.acme.gast.entity.ZimmerInformation;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * Model-Klasse für Spring HATEOAS. @lombok.Data fasst die Annotationsn @ToString, @EqualsAndHashCode, @Getter, @Setter
 * und @RequiredArgsConstructor zusammen.
 * <img src="../../../../../asciidoc/GastModel.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 */
@JsonPropertyOrder({
    "id", "nachname", "vorname", "email", "istVip", "rang", "geburtsdatum", "homepage", "geschlecht", "zimmerInfo", "buchungen",
    "praeferenzen"
})
@Relation(collectionRelation = "gaeste", itemRelation = "gast")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@ToString(callSuper = true)
class GastModel extends RepresentationModel<GastModel> {
    private final String nachname;
    private final String vorname;

    @EqualsAndHashCode.Include
    private final String email;

    private final boolean istVip;
    private final int rang;
    private final LocalDate geburtsdatum;
    private final URL homepage;
    private final GeschlechtTyp geschlecht;
    private final List<PraeferenzTyp> praeferenzen;
    private final ZimmerInformation zimmerInfo;

    GastModel(final Gast gast) {
        nachname = gast.getNachname();
        vorname = gast.getVorname();
        email = gast.getEmail();
        istVip = gast.isIstVip();
        rang = gast.getRang();
        geburtsdatum = gast.getGeburtsdatum();
        homepage = gast.getHomepage();
        geschlecht = gast.getGeschlecht();
        praeferenzen = gast.getPraeferenzen();
        zimmerInfo = gast.getZimmerInfo();
    }
}
