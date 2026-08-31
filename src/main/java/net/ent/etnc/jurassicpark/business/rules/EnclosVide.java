package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
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
            Intervention intervention
    ) {
        if (enclosService.enclosVide(intervention.getEnclos().getId(), intervention.getDateDebut(), intervention.getDateFin())){
            //TODO vérifier que l'on récupère bien le bon enclos avec la methode getEnclo (voir classe Intervention)
            return ResultatValidation.valide();
        } else {
            return ResultatValidation.invalide(ErreurValidation.ENCLOS_NON_VIDE);
        }
    }

}