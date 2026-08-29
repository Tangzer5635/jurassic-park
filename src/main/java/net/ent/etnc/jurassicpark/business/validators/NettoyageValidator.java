package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.EnclosVide;
import net.ent.etnc.jurassicpark.business.rules.PersonnelDisponible;
import net.ent.etnc.jurassicpark.business.rules.PersonnelQualifie;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.Personnel;

import java.util.List;

public class NettoyageValidator {

    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;
    private final EnclosVide enclosVide;

    public NettoyageValidator(
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible,
            EnclosVide enclosVide) {

        this.personnelQualifie = personnelQualifie;
        this.personnelDisponible = personnelDisponible;
        this.enclosVide = enclosVide;
    }

    public ResultatValidation valider(
            Personnel personnel,
            Enclos enclos,
            Intervention intervention
    ) {
        return ResultatValidation.combiner(List.of(
                personnelQualifie.verifier(personnel, intervention.getType()),
                personnelDisponible.verifier(personnel, intervention),
                enclosVide.verifier(enclos, intervention)
        ));
    }
}
