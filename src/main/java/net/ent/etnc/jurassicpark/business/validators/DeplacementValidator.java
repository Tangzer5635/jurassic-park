package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.*;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.Personnel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeplacementValidator {

    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;
    private final AnimalDisponible animalDisponible;
    private final EnclosDestinationCompatible enclosCompatible;
    private final AnimauxCompatibles animauxCompatibles;

    public DeplacementValidator(
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible,
            AnimalDisponible animalDisponible,
            EnclosDestinationCompatible enclosCompatible,
            AnimauxCompatibles animauxCompatibles) {

        this.personnelQualifie = personnelQualifie;
        this.personnelDisponible = personnelDisponible;
        this.animalDisponible = animalDisponible;
        this.enclosCompatible = enclosCompatible;
        this.animauxCompatibles = animauxCompatibles;
    }

    public ResultatValidation valider(
            Personnel personnel,
            Animal animal,
            List<Animal> animauxDejaPresents,
            Enclos enclos,
            Intervention intervention
    ) {
        return ResultatValidation.combiner(List.of(
                personnelQualifie.verifier(personnel, intervention.getType()),
                personnelDisponible.verifier(personnel, intervention),
                animalDisponible.verifier(animal, intervention),
                enclosCompatible.verifier(animal, enclos),
                animauxCompatibles.verifier(animal, animauxDejaPresents)
                )
        );
    }
}
