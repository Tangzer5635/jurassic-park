package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.*;
import net.ent.etnc.jurassicpark.models.Intervention;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NourrissageSoinValidator {

    private final AnimalPresent animalPresent;
    private final ManipulationVivants manipVivants;
    private final AnimalDisponible animalDisponible;
    private final PersonnelPresent personnelPresent;
    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;

    public NourrissageSoinValidator(
            AnimalDisponible animalDisponible,
            ManipulationVivants manipVivants,
            AnimalPresent animalPresent,
            PersonnelPresent personnelPresent,
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible) {

        this.animalPresent = animalPresent;
        this.manipVivants = manipVivants;
        this.animalDisponible = animalDisponible;
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
                animalDisponible.verifier(intervention),
                personnelPresent.verifier(intervention.getPersonnels()),
                personnelQualifie.verifier(intervention),
                personnelDisponible.verifier(intervention)
        ));
    }
}
