package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.InterventionDto;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.services.InterventionService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterventionAssembler {

    private final InterventionService interventionService;

    @Autowired
    public InterventionAssembler(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

}