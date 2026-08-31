package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Personnel;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PersonnelPresent {

    public ResultatValidation verifier(
            Set<Personnel> personnels
    ) {

        if (personnels.isEmpty()) {
            return ResultatValidation.invalide(ErreurValidation.AUCUN_PERSONNEL);
        } else {
            return ResultatValidation.valide();
        }
    }

}