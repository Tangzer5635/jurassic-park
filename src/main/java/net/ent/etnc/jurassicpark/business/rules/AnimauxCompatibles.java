package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.enumerations.Alimentation;
import net.ent.etnc.jurassicpark.models.enumerations.Dangerosite;
import net.ent.etnc.jurassicpark.services.AnimalService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AnimauxCompatibles {

    private final AnimalService animalService;

    public AnimauxCompatibles(AnimalService animalService) {
        this.animalService = animalService;
    }

    public ResultatValidation verifier(
            Intervention intervention
    ) {

        Set<Animal> animauxDeplaces = intervention.getAnimals();
        Set<Animal> animauxEnclos = animalService.getAnimauxEnclosApresInterventionsPlanifiees(intervention.getEnclos().getId());

        if ((animauxDeplaces.size() + animauxEnclos.size()) > intervention.getEnclos().getCapaciteMax()) {
            return ResultatValidation.invalide(ErreurValidation.ENCLOS_TROP_PETIT);
        }

        if (animauxDeplaces.isEmpty() || animauxEnclos.isEmpty()) {
            return ResultatValidation.valide();
        }

        Set<Espece> especesDeplacees = new HashSet<>();
        Set<Espece> especesEnclos = new HashSet<>();

        for (Animal a : animauxDeplaces) {
            especesDeplacees.add(a.getEspece());
        }
        for (Animal a : animauxEnclos) {
            especesEnclos.add(a.getEspece());
        }

        for (Espece espece : especesEnclos) {
            if (espece.getDangerosite().equals(Dangerosite.CRITIQUE) && !especesDeplacees.contains(espece)) {
                return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
            }
        }

        for (Espece espece : especesDeplacees) {

            if (!especesEnclos.contains(espece)) {

                if (espece.getDangerosite().equals(Dangerosite.CRITIQUE)) {
                    return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
                }

                switch (espece.getAlimentation()) {

                    case OMNIVORE, CARNIVORE -> {

                        for (Espece e : especesEnclos) {

                            if ((e.getAlimentation().equals(Alimentation.CARNIVORE)
                                    || e.getAlimentation().equals(Alimentation.OMNIVORE))) {

                                return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
                            }

                            if (e.getDangerosite().getNiveauDangerosite()
                                    <= espece.getDangerosite().getNiveauDangerosite()) {

                                return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
                            }
                        }
                    }

                    case HERBIVORE -> {

                        for (Espece e : especesEnclos) {

                            if ((e.getAlimentation().equals(Alimentation.CARNIVORE)
                                    || e.getAlimentation().equals(Alimentation.OMNIVORE))
                                    && e.getDangerosite().getNiveauDangerosite() >= espece.getDangerosite().getNiveauDangerosite()) {

                                return ResultatValidation.invalide(ErreurValidation.ANIMAUX_INCOMPATIBLES);
                            }

                        }
                    }

                }
            }
        }

        return ResultatValidation.valide();
    }
}