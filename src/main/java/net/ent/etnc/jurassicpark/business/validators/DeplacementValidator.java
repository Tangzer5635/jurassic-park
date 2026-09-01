package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.*;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Intervention;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeplacementValidator {

    private final AnimalPresent animalPresent;
    private final AnimalDejaDansEnclos animalDejaDansEnclos;
    private final ManipulationVivants manipVivants;
    private final AnimalDisponible animalDisponible;
    private final AnimauxCompatibles animauxCompatibles;
    private final EnclosTropPetit enclosTropPetit;
    private final EnclosDestinationCompatible enclosCompatible;
    private final NombrePersonnelSuffisant nombrePersonnelSuffisant;
    private final PersonnelPresent personnelPresent;
    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;

    public DeplacementValidator(
            AnimalPresent animalPresent,
            AnimalDejaDansEnclos animalDejaDansEnclos,
            ManipulationVivants manipVivants,
            AnimalDisponible animalDisponible,
            AnimauxCompatibles animauxCompatibles,
            EnclosTropPetit enclosTropPetit,
            EnclosDestinationCompatible enclosCompatible,
            NombrePersonnelSuffisant nombrePersonnelSuffisant,
            PersonnelPresent personnelPresent,
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible) {

        this.animalPresent = animalPresent;
        this.animalDejaDansEnclos = animalDejaDansEnclos;
        this.manipVivants = manipVivants;
        this.animalDisponible = animalDisponible;
        this.animauxCompatibles = animauxCompatibles;
        this.enclosTropPetit = enclosTropPetit;
        this.enclosCompatible = enclosCompatible;
        this.nombrePersonnelSuffisant = nombrePersonnelSuffisant;
        this.personnelPresent = personnelPresent;
        this.personnelQualifie = personnelQualifie;
        this.personnelDisponible = personnelDisponible;
    }

    public ResultatValidation valider(Intervention intervention) {
        return ResultatValidation.combiner(List.of(
                animalPresent.verifier(intervention.getAnimals()),
                animalDejaDansEnclos.verifier(intervention),
                manipVivants.verifier(intervention.getAnimals()),
                animalDisponible.verifier(intervention),
                animauxCompatibles.verifier(intervention),
                enclosTropPetit.verifier(intervention),
                enclosCompatible.verifier(intervention),
                nombrePersonnelSuffisant.verifier(intervention.getType(), intervention.getPersonnels().size()),
                personnelPresent.verifier(intervention.getPersonnels()),
                personnelQualifie.verifier(intervention),
                personnelDisponible.verifier(intervention)
        ));
    }
}