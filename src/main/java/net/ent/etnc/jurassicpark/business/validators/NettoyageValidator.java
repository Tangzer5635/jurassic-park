package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.EnclosVide;
import net.ent.etnc.jurassicpark.business.rules.PersonnelDisponible;
import net.ent.etnc.jurassicpark.business.rules.PersonnelPresent;
import net.ent.etnc.jurassicpark.business.rules.PersonnelQualifie;
import net.ent.etnc.jurassicpark.models.Intervention;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NettoyageValidator {

    private final PersonnelPresent personnelPresent;
    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;
    private final EnclosVide enclosVide;

    public NettoyageValidator(
            PersonnelPresent personnelPresent,
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible,
            EnclosVide enclosVide) {

        this.personnelPresent = personnelPresent;
        this.personnelQualifie = personnelQualifie;
        this.personnelDisponible = personnelDisponible;
        this.enclosVide = enclosVide;
    }

    public ResultatValidation valider(
            Intervention intervention
    ) {
        return ResultatValidation.combiner(List.of(
                personnelPresent.verifier(intervention.getPersonnels()),
                personnelQualifie.verifier(intervention),
                personnelDisponible.verifier(intervention),
                enclosVide.verifier(intervention)
        ));
    }
}
