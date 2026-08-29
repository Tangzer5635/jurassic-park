package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Enclos;
import org.springframework.stereotype.Component;

@Component
public class EnclosDestinationCompatible {

    public ResultatValidation verifier(
            Animal animal,
            Enclos enclos
    ) {

        if (animal.getEspece().getEnclos().contains(enclos)
            &&
                animal.getEspece().getDangerosite().getSecuriteMinimaleRequise().getSecuriteEnclos()
                <=
                enclos.getNiveauSecurite().getSecuriteEnclos()) {

            return ResultatValidation.valide();
        } else {
            return ResultatValidation.invalide(ErreurValidation.ENCLOS_INCOMPATIBLE);
        }
    }

}