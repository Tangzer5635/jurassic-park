package net.ent.etnc.jurassicpark.models.enumerations;

public enum NiveauHabilitation {

    JUNIOR(10),
    CONFIRME(20),
    EXPERT(30),
    ELITE(40);

    private final int niveauHabilitationInt;

    private NiveauHabilitation(int niveauHabilitation) {
        this.niveauHabilitationInt = niveauHabilitationInt;
    }

    public int getNiveauHabilitationInt() {return niveauHabilitationInt;}
}
