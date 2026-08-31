package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PersonnelQualifie {

    public ResultatValidation verifier(
            Intervention intervention
    ) {

        int minimumRequis = intervention.getType().getNiveauMinimumRequis().getNiveauHabilitationInt();

        for (Personnel p : intervention.getPersonnels()) {
            if (p.getNiveauHabilitation().getNiveauHabilitationInt() < minimumRequis) {
                return ResultatValidation.invalide(ErreurValidation.PERSONNEL_NON_QUALIFIE);
            }
        }

        return ResultatValidation.valide();
    }

}
