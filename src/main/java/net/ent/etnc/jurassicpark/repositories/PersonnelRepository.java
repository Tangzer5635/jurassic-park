package net.ent.etnc.jurassicpark.repositories;

import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.models.enumerations.NiveauHabilitation;
import net.ent.etnc.jurassicpark.repositories.commons.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PersonnelRepository extends BaseRepository<Personnel> {

    @Query("""
        SELECT COUNT(i) = 0 FROM Intervention i
        JOIN i.personnels p
        WHERE p.id = :personnelId
          AND i.etat <> net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention.ANNULEE
          AND i.dateDebut < :dateFin
          AND i.dateFin > :dateDebut
        """)
    boolean personnelEstLibre(@Param("personnelId") Long personnelId, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin);

    Page<Personnel> findAllByNiveauHabilitation(NiveauHabilitation niveau, Pageable pageable);
}