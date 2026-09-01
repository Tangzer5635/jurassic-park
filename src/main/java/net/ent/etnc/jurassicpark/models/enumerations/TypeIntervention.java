package net.ent.etnc.jurassicpark.models.enumerations;

public enum TypeIntervention {

    NOURRISSAGE(NiveauHabilitation.JUNIOR, 1),
    NETTOYAGE(NiveauHabilitation.JUNIOR, 1),
    SURVEILLANCE(NiveauHabilitation.CONFIRME, 1),
    DEPLACEMENT(NiveauHabilitation.CONFIRME, 2),
    EQUARRISSAGE(NiveauHabilitation.CONFIRME, 2),
    SOIN_MEDICAL(NiveauHabilitation.EXPERT, 1),
    CAPTURE_URGENTE(NiveauHabilitation.ELITE, 3);

    private final NiveauHabilitation niveauHabilitation;

    private final int nombreMinimumPersonnel;

    private TypeIntervention(NiveauHabilitation niveauHabilitation, int nombreMinimumPersonnel) {
        this.niveauHabilitation = niveauHabilitation;
        this.nombreMinimumPersonnel = nombreMinimumPersonnel;
    }

    public NiveauHabilitation getNiveauMinimumRequis() {
        return niveauHabilitation;
    }

    public int getNombreMinimumPersonnel() {
        return nombreMinimumPersonnel;
    }
}
