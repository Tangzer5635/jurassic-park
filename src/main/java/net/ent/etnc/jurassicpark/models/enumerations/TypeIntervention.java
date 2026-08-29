package net.ent.etnc.jurassicpark.models.enumerations;

public enum TypeIntervention {

    NOURRISSAGE(NiveauHabilitation.JUNIOR),
    NETTOYAGE(NiveauHabilitation.JUNIOR),
    SURVEILLANCE(NiveauHabilitation.CONFIRME),
    DEPLACEMENT(NiveauHabilitation.CONFIRME),
    SOIN_MEDICAL(NiveauHabilitation.EXPERT),
    CAPTURE_URGENTE(NiveauHabilitation.ELITE);

    private final NiveauHabilitation niveauHabilitation;

    private TypeIntervention(NiveauHabilitation niveauHabilitation) {
        this.niveauHabilitation = niveauHabilitation;
    }

    public NiveauHabilitation getNiveauMinimumRequis() {
        return niveauHabilitation;
    }
}
