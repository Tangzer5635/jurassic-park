package net.ent.etnc.jurassicpark.business.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record ResultatValidation(
        List<ErreurValidation> erreurs
) {

    public ResultatValidation(List<ErreurValidation> erreurs) {
        this.erreurs = List.copyOf(erreurs);
    }

    public static ResultatValidation valide() {
        return new ResultatValidation(List.of());
    }

    public static ResultatValidation invalide(ErreurValidation erreur) {
        return new ResultatValidation(List.of(erreur));
    }

    public static ResultatValidation combiner(Collection<ResultatValidation> resultats) {
        List<ErreurValidation> erreurs = new ArrayList<>();

        for (ResultatValidation resultat : resultats) {
            erreurs.addAll(resultat.erreurs());
        }

        return new ResultatValidation(erreurs);
    }

    public boolean estValide() {
        return erreurs.isEmpty();
    }
}
