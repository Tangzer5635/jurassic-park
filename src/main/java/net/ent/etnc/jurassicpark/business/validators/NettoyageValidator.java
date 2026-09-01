package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.rules.*;
import net.ent.etnc.jurassicpark.models.Intervention;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NettoyageValidator {

    private final NombrePersonnelSuffisant nombrePersonnelSuffisant;
    private final PersonnelPresent personnelPresent;
    private final PersonnelQualifie personnelQualifie;
    private final PersonnelDisponible personnelDisponible;
    private final EnclosVide enclosVide;

    public NettoyageValidator(
            NombrePersonnelSuffisant nombrePersonnelSuffisant,
            PersonnelPresent personnelPresent,
            PersonnelQualifie personnelQualifie,
            PersonnelDisponible personnelDisponible,
            EnclosVide enclosVide) {

        this.nombrePersonnelSuffisant = nombrePersonnelSuffisant;
        this.personnelPresent = personnelPresent;
        this.personnelQualifie = personnelQualifie;
        this.personnelDisponible = personnelDisponible;
        this.enclosVide = enclosVide;
    }

    public ResultatValidation valider(Intervention intervention) {
        return ResultatValidation.combiner(List.of(
                nombrePersonnelSuffisant.verifier(intervention.getType(), intervention.getPersonnels().size()),
                personnelPresent.verifier(intervention.getPersonnels()),
                personnelQualifie.verifier(intervention),
                personnelDisponible.verifier(intervention),
                enclosVide.verifier(intervention)
        ));
    }
}