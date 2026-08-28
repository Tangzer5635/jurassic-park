package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.EnclosDto;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnclosAssembler {

    private final EnclosService enclosService;

    @Autowired
    public EnclosAssembler(EnclosService enclosService) {
        this.enclosService = enclosService;
    }

}