package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.services.EnclosService;
import org.springframework.stereotype.Component;

@Component
public class EnclosVide {

    private final EnclosService enclosService;

    public EnclosVide(EnclosService enclosService) {
        this.enclosService = enclosService;
    }

    public ResultatValidation verifier(
            Enclos enclos,
            Intervention intervention
    ) {
        if (enclosService.enclosVide(enclos.getId())){
            return ResultatValidation.valide();
        } else {
            return ResultatValidation.invalide(ErreurValidation.ENCLOS_NON_VIDE);
        }
    }

}