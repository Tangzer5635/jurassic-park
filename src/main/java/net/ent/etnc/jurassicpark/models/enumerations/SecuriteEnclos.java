package net.ent.etnc.jurassicpark.models.enumerations;

public enum SecuriteEnclos {

    STANDARD(10),
    RENFORCE(20),
    MAXIMUM(30);

    private final int securiteEnclos;

    private SecuriteEnclos(int securiteEnclos) {this.securiteEnclos = securiteEnclos;}

    public int getSecuriteEnclos() {return securiteEnclos;}
}
