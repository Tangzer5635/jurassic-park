package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.enumerations.EtatSante;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ManipulationVivants {

    public ResultatValidation verifier(
            Set<Animal> animals
    ) {

        for (Animal a : animals) {
            if (a.getEtatSante().equals(EtatSante.DECEDE)){
                return ResultatValidation.invalide(ErreurValidation.MANIPULATION_ANIMAL_MORT);
            }
        }

        return ResultatValidation.valide();
    }

}