package com.acme.gast.repository;

import com.acme.gast.entity.Gast;
import com.acme.gast.entity.Gast_;
import com.acme.gast.entity.GeschlechtTyp;
import com.acme.gast.entity.PraeferenzTyp;
import com.acme.gast.entity.ZimmerInformation_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton-Klasse, um Specifications für Queries in Spring Data JPA zu bauen.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 */
// TODO https://github.com/checkstyle/checkstyle/issues/14444
@Component
@Slf4j
@SuppressWarnings({"LambdaParameterName", "IllegalIdentifierName"})
public class SpecificationBuilder {
    /**
     * Specification für eine Query mit Spring Data bauen.
     *
     * @param queryParams als MultiValueMap
     * @return Specification für eine Query mit Spring Data
     */
    public Optional<Specification<Gast>> build(final Map<String, ? extends List<String>> queryParams) {
        log.debug("build: queryParams={}", queryParams);

        if (queryParams.isEmpty()) {
            // keine Suchkriterien
            return Optional.empty();
        }

        final var specs = queryParams
            .entrySet()
            .stream()
            .map(this::toSpecification)
            .toList();

        if (specs.isEmpty() || specs.contains(null)) {
            return Optional.empty();
        }

        return Optional.of(Specification.allOf(specs));
    }

    @SuppressWarnings("CyclomaticComplexity")
    private Specification<Gast> toSpecification(final Map.Entry<String, ? extends List<String>> entry) {
        log.trace("toSpec: entry={}", entry);
        final var key = entry.getKey();
        final var values = entry.getValue();
        if ("praeferenz".contentEquals(key)) {
            return toSpecificationPraeferenzen(values);
        }

        if (values == null || values.size() != 1) {
            return null;
        }

        final var value = values.getFirst();
        return switch (key) {
            case "nachname" -> nachname(value);
            case "email" ->  email(value);
            case "rang" -> rang(value);
            case "vip" -> vip(value);
            case "geschlecht" -> geschlecht(value);
            case "zimmerNummer" -> zimmerNummer(value);
            default -> null;
        };
    }

    private Specification<Gast> toSpecificationPraeferenzen(final Collection<String> praeferenzen) {
        log.trace("build: praeferenzen={}", praeferenzen);
        if (praeferenzen == null || praeferenzen.isEmpty()) {
            return null;
        }

        final var specsImmutable = praeferenzen.stream()
            .map(this::praeferenz)
            .toList();
        if (specsImmutable.isEmpty() || specsImmutable.contains(null)) {
            return null;
        }

        final List<Specification<Gast>> specs = new ArrayList<>(specsImmutable);
        final var first = specs.removeFirst();
        return specs.stream().reduce(first, Specification::and);
    }

    private Specification<Gast> nachname(final String teil) {
        return (root, _, builder) -> builder.like(
            builder.lower(root.get(Gast_.nachname)),
            builder.lower(builder.literal("%" + teil + '%'))
        );
    }

    private Specification<Gast> email(final String teil) {
        return (root, _, builder) -> builder.like(
            builder.lower(root.get(Gast_.email)),
            builder.lower(builder.literal("%" + teil + '%'))
        );
    }

    @SuppressWarnings({"CatchParameterName", "LocalFinalVariableName"})
    private Specification<Gast> rang(final String rang) {
        final int rangInt;
        try {
            rangInt = Integer.parseInt(rang);
        } catch (final NumberFormatException _) {
            //noinspection ReturnOfNull
            return null;
        }
        return (root, _, builder) -> builder.equal(root.get(Gast_.rang), rangInt);
    }

    private Specification<Gast> vip(final String istVip) {
        return (root, _, builder) -> builder.equal(
            root.get(Gast_.istVip),
            Boolean.parseBoolean(istVip)
        );
    }

    private Specification<Gast> geschlecht(final String geschlecht) {
        return (root, _, builder) -> builder.equal(
            root.get(Gast_.geschlecht),
            GeschlechtTyp.of(geschlecht)
        );
    }
    

    private Specification<Gast> praeferenz(final String praeferenz) {
        final var praeferenzEnum = PraeferenzTyp.of(praeferenz);
        if (praeferenzEnum == null) {
            return null;
        }
        return (root, _, builder) -> builder.like(
            root.get(Gast_.praeferenzenStr),
            builder.literal("%" + praeferenzEnum.name() + '%')
        );
    }

    private Specification<Gast> zimmerNummer(final String prefix) {
        return (root, _, builder) -> builder.like(root.get(Gast_.zimmerInfo).get(ZimmerInformation_.zimmerNummer), prefix + '%');
    }
}
