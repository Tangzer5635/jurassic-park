package net.ent.etnc.jurassicpark.services;

import jakarta.validation.constraints.NotNull;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.services.commons.Service;

import java.time.LocalDateTime;

public interface EnclosService extends Service<Enclos, Long> {

    boolean enclosVide(Long id, @NotNull(message = "dateDebut ne doit pas être null") LocalDateTime dateDebut, @NotNull(message = "dateFin ne doit pas être null") LocalDateTime dateFin);
}