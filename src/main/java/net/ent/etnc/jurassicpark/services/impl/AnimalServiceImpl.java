package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.repositories.AnimalRepository;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnimalServiceImpl extends AbstractService<Animal, AnimalRepository> implements AnimalService {

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository) {
        super(animalRepository);
    }

    @Override
    public boolean animalEstDisponible(Long id, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return false;
    }
}