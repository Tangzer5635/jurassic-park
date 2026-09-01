package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.services.AnimalService;
import org.springframework.stereotype.Component;

@Component
public class AnimalDisponible {

    private final AnimalService animalService;

    public AnimalDisponible(AnimalService animalService) {
        this.animalService = animalService;
    }

    public ResultatValidation verifier(
            Intervention intervention
    ) {

        for (Animal a : intervention.getAnimals()) {
            if (!animalService.animalEstDisponible(a.getId(), intervention.getId(), intervention.getDateDebut(), intervention.getDateFin())) {
                return ResultatValidation.invalide(ErreurValidation.ANIMAL_NON_DISPONIBLE);
            }
        }

        return ResultatValidation.valide();
    }

}