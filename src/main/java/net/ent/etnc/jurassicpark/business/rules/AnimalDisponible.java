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
            Animal animal,
            Intervention intervention
    ) {

        if (animalService.animalEstDisponible(animal.getId(), intervention.getDateDebut(), intervention.getDateFin())) {
            return ResultatValidation.valide();
        } else {
            return ResultatValidation.invalide(ErreurValidation.ANIMAL_NON_DISPONIBLE);
        }
    }

}