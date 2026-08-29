package net.ent.etnc.jurassicpark.services;

import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.services.commons.Service;

public interface EnclosService extends Service<Enclos, Long> {

    boolean enclosVide(Long id);
}