package net.ent.etnc.jurassicpark.services;

import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.services.commons.Service;

public interface InterventionService extends Service<Intervention, Long> {

    boolean animalUtilise(Long animalId);
    boolean personnelUtilise(Long personnelId);
    boolean enclosUtilise(Long enclosId);

}