package net.ent.etnc.jurassicpark.services;

import jakarta.validation.constraints.NotNull;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.services.commons.Service;

import java.time.LocalDateTime;

public interface AnimalService extends Service<Animal, Long> {

    boolean animalEstDisponible(Long id, @NotNull(message = "dateDebut ne doit pas être null") LocalDateTime dateDebut, @NotNull(message = "dateFin ne doit pas être null") LocalDateTime dateFin);

    boolean existeParEspece(Long especeId);
    boolean existeParEnclos(Long enclosId);
}