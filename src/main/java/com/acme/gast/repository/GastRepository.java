package com.acme.gast.repository;

import com.acme.gast.entity.Gast;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.acme.gast.entity.Gast.ZIMMER_INFO_BUCHUNGEN_GRAPH;
import static com.acme.gast.entity.Gast.ZIMMER_INFO_GRAPH;

/**
 * Repository für den DB-Zugriff bei Gaeste.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 */
@Repository
public interface GastRepository extends JpaRepository<Gast, UUID>, JpaSpecificationExecutor<Gast> {
    @EntityGraph(ZIMMER_INFO_GRAPH)
    @NonNull
    @Override
    List<Gast> findAll();

    @EntityGraph(ZIMMER_INFO_GRAPH)
    @NonNull
    @Override
    List<Gast> findAll(@NonNull Specification<Gast> spec);

    @EntityGraph(ZIMMER_INFO_GRAPH)
    @NonNull
    @Override
    Optional<Gast> findById(@NonNull UUID id);

    /**
     * Gast einschließlich Umsätze anhand der ID suchen.
     *
     * @param id Gast ID
     * @return Gefundener Gast
     */
    @Query("""
        SELECT DISTINCT k
        FROM     #{#entityName} k
        WHERE    k.id = :id
        """)
    @EntityGraph(ZIMMER_INFO_BUCHUNGEN_GRAPH)
    @NonNull
    Optional<Gast> findByIdFetchBuchungen(UUID id);

    /**
     * Gast zu gegebener Emailadresse aus der DB ermitteln.
     *
     * @param email Emailadresse für die Suche
     * @return Optional mit dem gefundenen Gast oder leeres Optional
     */
    @Query("""
        SELECT k
        FROM   #{#entityName} k
        WHERE  lower(k.email) LIKE concat(lower(:email), '%')
        """)
    @EntityGraph(ZIMMER_INFO_GRAPH)
    Optional<Gast> findByEmail(String email);

    /**
     * Abfrage, ob es einen Gaeste mit gegebener Emailadresse gibt.
     *
     * @param email Emailadresse für die Suche
     * @return true, falls es einen solchen Gaeste gibt, sonst false
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean existsByEmail(String email);

    /**
     * Gaeste anhand des Nachnamens suchen.
     *
     * @param nachname Der (Teil-) Nachname der gesuchten Gaeste
     * @return Die gefundenen Gaeste oder eine leere Collection
     */
    @Query("""
        SELECT   k
        FROM     #{#entityName} k
        WHERE    lower(k.nachname) LIKE concat('%', lower(:nachname), '%')
        ORDER BY k.nachname
        """)
    @EntityGraph(ZIMMER_INFO_GRAPH)
    List<Gast> findByNachname(CharSequence nachname);

    /**
     * Abfrage, welche Nachnamen es zu einem Präfix gibt.
     *
     * @param prefix Nachname-Präfix.
     * @return Die passenden Nachnamen oder eine leere Collection.
     */
    @Query("""
        SELECT DISTINCT k.nachname
        FROM     #{#entityName} k
        WHERE    lower(k.nachname) LIKE concat(lower(:prefix), '%')
        ORDER BY k.nachname
        """)
    List<String> findNachnamenByPrefix(String prefix);
}
