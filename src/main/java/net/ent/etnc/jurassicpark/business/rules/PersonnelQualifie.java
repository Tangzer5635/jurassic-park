package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;
import org.springframework.stereotype.Component;

@Component
public class PersonnelQualifie {

    public ResultatValidation verifier(
            Personnel personnel,
            TypeIntervention typeIntervention
    ) {

        if (typeIntervention.getNiveauMinimumRequis().getNiveauHabilitationInt()
                <=
                personnel.getNiveauHabilitation().getNiveauHabilitationInt()) {
            return ResultatValidation.valide();
        } else {
            return ResultatValidation.invalide(ErreurValidation.PERSONNEL_NON_QUALIFIE);
        }
    }

}
