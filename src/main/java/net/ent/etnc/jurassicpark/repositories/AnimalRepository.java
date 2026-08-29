package net.ent.etnc.jurassicpark.repositories;

import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.repositories.commons.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;

@Repository
public interface AnimalRepository extends BaseRepository<Animal> {

    @Query("SELECT COUNT(a) = 0 FROM Animal a WHERE a.enclos.id = :enclosId")
    boolean enclosEstVide(@Param("enclosId") Long enclosId);

    @Query("""
        SELECT COUNT(i) = 0 FROM Intervention i
        JOIN i.animals a
        WHERE a.id = :animalId
          AND i.etat <> net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention.ANNULEE
          AND i.dateDebut < :dateFin
          AND i.dateFin > :dateDebut
        """)
    boolean animalEstLibre(@Param("animalId") Long animalId, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

    boolean existsByEspeceId(Long especeId);
    boolean existsByEnclosId(Long enclosId);

}