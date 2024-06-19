package com.acme.gast.controller;

import com.acme.gast.controller.ExcludeFromJacocoGeneratedReport;
import com.acme.gast.entity.ZimmerInformation;
import com.acme.gast.entity.Gast;
import com.acme.gast.entity.Buchung;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * Mapper zwischen Entity-Klassen. Siehe build\generated\sources\annotationProcessor\java\main\...\GastMapperImpl.java.
 *
 * @author <a href="mailto:Caleb_g@outlook.de">Caleb Gyamfi</a>
 */
@Mapper(nullValueIterableMappingStrategy = RETURN_DEFAULT, componentModel = "spring")
@AnnotateWith(ExcludeFromJacocoGeneratedReport.class)
interface GastMapper {
    /**
     * Ein DTO-Objekt von GastDTO in ein Objekt für ein neues Gast-Objekt konvertieren.
     *
     * @param dto DTO-Objekt für GastDTO ohne ID, version, erzeugt, aktualisiert, praeferenzStr, username
     * @return Konvertiertes Gast-Objekt mit null als ID
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "erzeugt", ignore = true)
    @Mapping(target = "aktualisiert", ignore = true)
    @Mapping(target = "praeferenzenStr", ignore = true)
    Gast toGast(GastDTO dto);

    /**
     * Ein DTO-Objekt von ZimmerInformationDTO in ein Objekt für ZimmerInformation konvertieren.
     *
     * @param dto DTO-Objekt für ZimmerInformationDTO ohne ID und gast
     * @return Konvertiertes ZimmerInformation-Objekt mit null als ID
     */
    @Mapping(target = "id", ignore = true)
    ZimmerInformation toZimmerInformation(ZimmerInformationDTO dto);

    /**
     * Ein DTO-Objekt von BuchungDTO in ein Objekt für Buchung konvertieren.
     *
     * @param dto DTO-Objekt für BuchungDTO ohne ID und gast
     * @return Konvertiertes Buchung-Objekt mit null als ID
     */
    @Mapping(target = "id", ignore = true)
    Buchung toBuchung(BuchungDTO dto);
}
