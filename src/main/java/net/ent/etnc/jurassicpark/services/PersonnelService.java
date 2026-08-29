package net.ent.etnc.jurassicpark.services;

import jakarta.validation.constraints.NotNull;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.services.commons.Service;

import java.time.LocalDateTime;

public interface PersonnelService extends Service<Personnel, Long> {

    boolean personnelEstDisponnible(Long id, @NotNull(message = "dateDebut ne doit pas être null") LocalDateTime dateDebut, @NotNull(message = "dateFin ne doit pas être null") LocalDateTime dateFin);
}