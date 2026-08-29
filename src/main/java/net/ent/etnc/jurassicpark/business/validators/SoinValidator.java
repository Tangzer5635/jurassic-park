package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.AnimalDisponible;
import net.ent.etnc.jurassicpark.business.rules.PersonnelDisponible;
import net.ent.etnc.jurassicpark.business.rules.PersonnelQualifie;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.Personnel;

import java.util.List;

public class SoinValidator {

    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;
    private final AnimalDisponible animalDisponible;

    public SoinValidator(
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible,
            AnimalDisponible animalDisponible) {

        this.personnelQualifie = personnelQualifie;
        this.personnelDisponible = personnelDisponible;
        this.animalDisponible = animalDisponible;
    }

    public ResultatValidation valider(
            Personnel personnel,
            Intervention intervention
    ) {
        return ResultatValidation.combiner(List.of(
                personnelQualifie.verifier(personnel, intervention.getType()),
                personnelDisponible.verifier(personnel, intervention)
        ));
    }
}
