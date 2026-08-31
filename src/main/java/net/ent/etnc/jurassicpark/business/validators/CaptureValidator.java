package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.*;
import net.ent.etnc.jurassicpark.models.Intervention;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaptureValidator {

    private final AnimalPresent animalPresent;
    private final ManipulationVivants manipVivants;
    private final PersonnelPresent personnelPresent;
    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;

    public CaptureValidator(
            AnimalPresent animalPresent,
            ManipulationVivants manipVivants,
            PersonnelPresent personnelPresent,
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible) {

        this.animalPresent = animalPresent;
        this.manipVivants = manipVivants;
        this.personnelPresent = personnelPresent;
        this.personnelQualifie = personnelQualifie;
        this.personnelDisponible = personnelDisponible;
    }

    public ResultatValidation valider(
            Intervention intervention
    ) {
        return ResultatValidation.combiner(List.of(
                animalPresent.verifier(intervention.getAnimals()),
                manipVivants.verifier(intervention.getAnimals()),
                personnelPresent.verifier(intervention.getPersonnels()),
                personnelQualifie.verifier(intervention),
                personnelDisponible.verifier(intervention)
        ));
    }
}
