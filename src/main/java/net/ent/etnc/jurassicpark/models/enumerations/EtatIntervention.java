package net.ent.etnc.jurassicpark.models.enumerations;

public enum EtatIntervention {
    PLANIFIEES,
    EN_COURS,
    TERMINES,
    ANNULEE;

    public boolean peutDevenir(EtatIntervention cible) {
        if (this == cible) {
            return true;
        }
        return switch (this) {
            case PLANIFIEES -> cible == EN_COURS || cible == ANNULEE;
            case EN_COURS -> cible == TERMINES || cible == ANNULEE;
            case TERMINES, ANNULEE -> false;
        };
    }
}