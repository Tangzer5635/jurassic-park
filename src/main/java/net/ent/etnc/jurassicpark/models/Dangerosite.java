package net.ent.etnc.jurassicpark.models;

public enum Dangerosite {

    FAIBLE(SecuriteEnclos.STANDARD),
    MODERE(SecuriteEnclos.STANDARD),
    ELEVE(SecuriteEnclos.RENFORCE),
    CRITIQUE(SecuriteEnclos.MAXIMUM);

    private final SecuriteEnclos securiteMinimaleRequise;

    private Dangerosite(SecuriteEnclos securiteMinimaleRequise) {
        this.securiteMinimaleRequise = securiteMinimaleRequise;
    }

    public SecuriteEnclos getSecuriteMinimaleRequise() {
        return securiteMinimaleRequise;
    }
}
