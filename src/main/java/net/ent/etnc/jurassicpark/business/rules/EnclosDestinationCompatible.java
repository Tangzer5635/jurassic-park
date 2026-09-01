package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.enumerations.EtatEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.EtatSante;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EnclosDestinationCompatible {

    public ResultatValidation verifier(
            Intervention intervention
    ) {

        Set<Animal> animaux = intervention.getAnimals();
        Enclos enclos = intervention.getEnclos();

        if (enclos == null) {
            return ResultatValidation.invalide(ErreurValidation.AUCUN_ENCLOS);
        }

        if (!enclos.getEtat().equals(EtatEnclos.ACTIF)) {
            return ResultatValidation.invalide(ErreurValidation.ENCLOS_INACTIF);
        }

        for (Animal animal : animaux) {

            if ((animal.getEtatSante().equals(EtatSante.BLESSE) || animal.getEtatSante().equals(EtatSante.MALADE))
                    && !enclos.getType().equals(TypeEnclos.QUARANTAINE)) {

                return ResultatValidation.invalide(ErreurValidation.ENCLOS_INCOMPATIBLE);
            }

            if (animal.getEspece().getDangerosite().getSecuriteMinimaleRequise().getSecuriteEnclos()
                    > enclos.getNiveauSecurite().getSecuriteEnclos()) {

                return ResultatValidation.invalide(ErreurValidation.SECURITE_INSUFFISANTE);
            }

//            if (!animal.getEspece().getEnclos().contains(enclos)) { //TODO vérifier le type d'enclos autoriser -> changer Espece ou faire un switch?
//
//                return ResultatValidation.invalide(ErreurValidation.ENCLOS_INCOMPATIBLE);
//            }
        }

        return ResultatValidation.valide();
    }
}