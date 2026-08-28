package net.ent.etnc.jurassicpark.models.enumerations;

public enum Dangerosite {

    FAIBLE(SecuriteEnclos.STANDARD, 10),
    MODERE(SecuriteEnclos.STANDARD, 20),
    ELEVE(SecuriteEnclos.RENFORCE, 30),
    CRITIQUE(SecuriteEnclos.MAXIMUM, 40);

    private final SecuriteEnclos securiteMinimaleRequise;

    private final int niveauDangerosite;

    private Dangerosite(SecuriteEnclos securiteMinimaleRequise, int niveauDangerosite) {
        this.securiteMinimaleRequise = securiteMinimaleRequise;
        this.niveauDangerosite = niveauDangerosite;
    }

    public SecuriteEnclos getSecuriteMinimaleRequise() {
        return securiteMinimaleRequise;
    }

    public int getNiveauDangerosite() {return niveauDangerosite;}
}
