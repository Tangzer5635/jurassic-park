package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.EspeceDto;
import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.services.EspeceService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EspeceAssembler {

    private final EspeceService especeService;

    @Autowired
    public EspeceAssembler(EspeceService especeService) {
        this.especeService = especeService;
    }

}