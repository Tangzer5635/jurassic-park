package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.services.AnimalService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AnimalDejaDansEnclos {

    private final AnimalService animalService;

    public AnimalDejaDansEnclos(AnimalService animalService) {
        this.animalService = animalService;
    }

    public ResultatValidation verifier(
            Intervention intervention
    ) {

        Set<Animal> animauxEnclos = animalService.getAnimauxEnclosApresInterventionsPlanifiees(intervention.getEnclos().getId(), intervention.getId());

        for (Animal a : intervention.getAnimals()) {
            if (animauxEnclos.contains(a)) {
                return ResultatValidation.invalide(ErreurValidation.ANIMAL_DEJA_DANS_ENCLOS);
            }
        }

        return ResultatValidation.valide();
    }
}
