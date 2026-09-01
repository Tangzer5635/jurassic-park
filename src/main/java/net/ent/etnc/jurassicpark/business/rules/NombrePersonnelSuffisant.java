package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;
import org.springframework.stereotype.Component;

@Component
public class NombrePersonnelSuffisant {

    public ResultatValidation verifier(TypeIntervention type, int nombrePersonnels) {
        if (nombrePersonnels >= type.getNombreMinimumPersonnel()) {
            return ResultatValidation.valide();
        }
        return ResultatValidation.invalide(ErreurValidation.PERSONNEL_INSUFFISANT);
    }
}