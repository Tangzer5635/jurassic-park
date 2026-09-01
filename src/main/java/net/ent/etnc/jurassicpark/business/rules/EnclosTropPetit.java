package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.services.AnimalService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EnclosTropPetit {

    private final AnimalService animalService;

    public EnclosTropPetit(AnimalService animalService) {
        this.animalService = animalService;
    }

    public ResultatValidation verifier(
            Intervention intervention
    ) {

        Set<Animal> animauxEnclos = animalService.getAnimauxEnclosApresInterventionsPlanifiees(intervention.getEnclos().getId(), intervention.getId());
        animauxEnclos.addAll(intervention.getAnimals());

        if (animauxEnclos.size() > intervention.getEnclos().getCapaciteMax()) {
            return ResultatValidation.invalide(ErreurValidation.ENCLOS_TROP_PETIT);
        }

        return ResultatValidation.valide();
    }

}
