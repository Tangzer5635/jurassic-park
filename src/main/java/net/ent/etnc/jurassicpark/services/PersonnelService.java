package net.ent.etnc.jurassicpark.services;

import jakarta.validation.constraints.NotNull;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.models.enumerations.NiveauHabilitation;
import net.ent.etnc.jurassicpark.services.commons.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PersonnelService extends Service<Personnel, Long> {

    boolean personnelEstDisponible(Long id, @NotNull(message = "dateDebut ne doit pas être null") LocalDateTime dateDebut, @NotNull(message = "dateFin ne doit pas être null") LocalDateTime dateFin);

    Page<Personnel> findAllByNiveauHabilitation(NiveauHabilitation niveau, Pageable pageable);
}