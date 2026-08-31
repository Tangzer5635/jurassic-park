package net.ent.etnc.jurassicpark.repositories;

import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.repositories.commons.BaseRepository;
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

}