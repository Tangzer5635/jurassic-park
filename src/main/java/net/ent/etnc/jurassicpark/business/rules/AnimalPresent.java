package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AnimalPresent {

    public ResultatValidation verifier(
            Set<Animal> animals
    ) {

        if (animals == null || animals.isEmpty()) {
            return ResultatValidation.invalide(ErreurValidation.AUCUN_ANIMAL);
        } else {
            return ResultatValidation.valide();
        }
    }

}