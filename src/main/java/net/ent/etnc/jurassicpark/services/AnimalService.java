package net.ent.etnc.jurassicpark.services;

import jakarta.validation.constraints.NotNull;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.services.commons.Service;

import java.time.LocalDateTime;
import java.util.Set;

public interface AnimalService extends Service<Animal, Long> {

    boolean animalEstDisponible(Long id, @NotNull(message = "dateDebut ne doit pas être null") LocalDateTime dateDebut, @NotNull(message = "dateFin ne doit pas être null") LocalDateTime dateFin);

    Set<Animal> getAnimauxEnclosApresInterventionsPlanifiees(Long idEnclos);
}