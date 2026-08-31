package net.ent.etnc.jurassicpark.repositories;

import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;
import net.ent.etnc.jurassicpark.repositories.commons.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InterventionRepository extends BaseRepository<Intervention> {

    @Query("""
        SELECT COUNT(i) = 0 FROM Intervention i
        JOIN i.animals a
        WHERE a.id = :animalId
          AND i.etat <> net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention.ANNULEE
          AND i.dateDebut < :dateFin
          AND i.dateFin > :dateDebut
        """)
    boolean animalEstLibre(@Param("animalId") Long animalId, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

    @Query("""
        SELECT COUNT(i) = 0 FROM Intervention i
        JOIN i.personnels p
        WHERE p.id = :personnelId
          AND i.etat <> net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention.ANNULEE
          AND i.dateDebut < :dateFin
          AND i.dateFin > :dateDebut
        """)
    boolean personnelEstLibre(@Param("personnelId") Long personnelId, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

    @Query("SELECT COUNT(i) > 0 " +
            "FROM Intervention i " +
            "JOIN i.animals a " +
            "WHERE a.id = :animalId")
    boolean animalUtilise(@Param("animalId") Long animalId);

    @Query("SELECT COUNT(i) > 0 " +
            "FROM Intervention i " +
            "JOIN i.personnels p " +
            "WHERE p.id = :personnelId")
    boolean personnelUtilise(@Param("personnelId") Long personnelId);

    boolean existsByEnclosId(Long enclosId);

    @Query("SELECT i " +
            "FROM Intervention i " +
            "JOIN i.animals a " +
            "WHERE a.id = :animalId")
    Page<Intervention> findAllByAnimalId(@Param("animalId") Long animalId, Pageable pageable);

    @Query("SELECT i " +
            "FROM Intervention i " +
            "JOIN i.personnels p " +
            "WHERE p.id = :personnelId")
    Page<Intervention> findAllByPersonnelId(@Param("personnelId") Long personnelId, Pageable pageable);

    Page<Intervention> findAllByEnclosId(Long enclosId, Pageable pageable);

    Page<Intervention> findAllByEtat(EtatIntervention etat, Pageable pageable);

    Page<Intervention> findAllByType(TypeIntervention type, Pageable pageable);

}