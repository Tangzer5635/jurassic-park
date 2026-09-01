package net.ent.etnc.jurassicpark.models.enumerations;

public enum EtatIntervention {
    PLANIFIEE,
    EN_COURS,
    TERMINEE,
    ANNULEE;

    public boolean peutDevenir(EtatIntervention cible) {
        if (this == cible) {
            return true;
        }
        return switch (this) {
            case PLANIFIEE -> cible == EN_COURS || cible == ANNULEE;
            case EN_COURS -> cible == TERMINEE || cible == ANNULEE;
            case TERMINEE, ANNULEE -> false;
        };
    }
}