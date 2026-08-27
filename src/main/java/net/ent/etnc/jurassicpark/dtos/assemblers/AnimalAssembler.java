package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.AnimalDto;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnimalAssembler {

    private final AnimalService animalService;

    @Autowired
    public AnimalAssembler(AnimalService animalService) {
        this.animalService = animalService;
    }

}