package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.enumerations.Alimentation;
import net.ent.etnc.jurassicpark.models.enumerations.Dangerosite;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnimauxCompatibles {

    public ResultatValidation verifier(
            Animal animal,
            List<Animal> animauxEnclos
    ) {
        if (animauxEnclos.isEmpty()) {
            return ResultatValidation.valide();
        }

        if (!animauxEnclos.getFirst().getEspece().equals(animal.getEspece())
                && animal.getEspece().getDangerosite().equals(Dangerosite.CRITIQUE)) {
            return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
        }

        return switch (animal.getEspece().getAlimentation()) {
            case OMNIVORE, CARNIVORE -> verifierCompatibiliteCarnivore(animal, animauxEnclos);
            case HERBIVORE -> verifierCompatibiliteHerbivore(animal, animauxEnclos);
        };
    }

    private ResultatValidation verifierCompatibiliteCarnivore(Animal animal, List<Animal> animauxEnclos) {

        for (Animal a : animauxEnclos) {

            if (!a.getEspece().equals(animal.getEspece()) &&
                    (a.getEspece().getAlimentation().equals(Alimentation.CARNIVORE)
                            || a.getEspece().getAlimentation().equals(Alimentation.OMNIVORE)
                            || a.getEspece().getDangerosite().equals(Dangerosite.CRITIQUE)) ) {

                return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
            }

            if (a.getEspece().getDangerosite().getNiveauDangerosite()
                <= animal.getEspece().getDangerosite().getNiveauDangerosite()) {

                return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
            }
        }

        return ResultatValidation.valide();
    }

    private ResultatValidation verifierCompatibiliteHerbivore(Animal animal, List<Animal> animauxEnclos) {

        for (Animal a : animauxEnclos) {

            if (!a.getEspece().equals(animal.getEspece())
                    && a.getEspece().getDangerosite().equals(Dangerosite.CRITIQUE)) {
                return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
            }

            if ( (a.getEspece().getAlimentation().equals(Alimentation.CARNIVORE)
                    || a.getEspece().getAlimentation().equals(Alimentation.OMNIVORE))
                && a.getEspece().getDangerosite().getNiveauDangerosite() >= animal.getEspece().getDangerosite().getNiveauDangerosite() ) {

                return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
            }


        }

        return ResultatValidation.valide();
    }



}