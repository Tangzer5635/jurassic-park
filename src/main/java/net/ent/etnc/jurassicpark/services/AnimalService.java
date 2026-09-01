package net.ent.etnc.jurassicpark.services;

import jakarta.validation.constraints.NotNull;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.enumerations.EtatSante;
import net.ent.etnc.jurassicpark.services.commons.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

public interface AnimalService extends Service<Animal, Long> {

    boolean animalEstDisponible(Long animalId, Long interventionId, @NotNull(message = "dateDebut ne doit pas être null") LocalDateTime dateDebut, @NotNull(message = "dateFin ne doit pas être null") LocalDateTime dateFin);

    Set<Animal> getAnimauxEnclosApresInterventionsPlanifiees(Long idEnclos);

    boolean existeParEnclos(Long enclosId);

    boolean existeParEspece(Long especeId);

    void deplacerCadavres(Set<Animal> animals);

    void deplacerAnimaux(Set<Animal> animals, Enclos enclos);

    Page<Animal> findAllByEnclosId(Long enclosId, Pageable pageable);

    Page<Animal> findAllByEspeceId(Long especeId, Pageable pageable);

    Page<Animal> findAllByEtatSante(EtatSante etatSante, Pageable pageable);

    void soignerAnimal(Set<Animal> animals);
}