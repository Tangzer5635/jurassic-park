package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import net.ent.etnc.jurassicpark.services.EnclosService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AnimalDejaDansCimetiere {

    private final EnclosService enclosService;

    public AnimalDejaDansCimetiere(EnclosService enclosService) {
        this.enclosService = enclosService;
    }

    public ResultatValidation verifier(
            Set<Animal> animaux
    ) {

        for (Animal a : animaux) {
            Enclos enclos = enclosService.findByAnimalId(a.getId());
            if (enclos.getType().equals(TypeEnclos.CIMETIERE)) {
                return ResultatValidation.invalide(ErreurValidation.ANIMAL_DEJA_DANS_CIMETIERE);
            }
        }

        return ResultatValidation.valide();
    }
}
