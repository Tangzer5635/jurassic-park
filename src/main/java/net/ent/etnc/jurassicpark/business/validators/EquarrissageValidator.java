package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.*;
import net.ent.etnc.jurassicpark.models.Intervention;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EquarrissageValidator {

    private final AnimalPresent animalPresent;
    private final ManipulationMorts manipMorts;
    private final NombrePersonnelSuffisant nombrePersonnelSuffisant;
    private final PersonnelPresent personnelPresent;
    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;

    public EquarrissageValidator(
            AnimalPresent animalPresent,
            ManipulationMorts manipMorts,
            NombrePersonnelSuffisant nombrePersonnelSuffisant,
            PersonnelPresent personnelPresent,
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible) {

        this.animalPresent = animalPresent;
        this.manipMorts = manipMorts;
        this.nombrePersonnelSuffisant = nombrePersonnelSuffisant;
        this.personnelPresent = personnelPresent;
        this.personnelQualifie = personnelQualifie;
        this.personnelDisponible = personnelDisponible;
    }

    public ResultatValidation valider(Intervention intervention) {
        return ResultatValidation.combiner(List.of(
                animalPresent.verifier(intervention.getAnimals()),
                manipMorts.verifier(intervention.getAnimals()),
                nombrePersonnelSuffisant.verifier(intervention.getType(), intervention.getPersonnels().size()),
                personnelPresent.verifier(intervention.getPersonnels()),
                personnelQualifie.verifier(intervention),
                personnelDisponible.verifier(intervention)
        ));
    }
}