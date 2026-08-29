package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.repositories.AnimalRepository;
import net.ent.etnc.jurassicpark.repositories.EnclosRepository;
import net.ent.etnc.jurassicpark.repositories.InterventionRepository;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.InterventionService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnclosServiceImpl extends AbstractService<Enclos, EnclosRepository> implements EnclosService {

    private final AnimalService animalService;
    private final InterventionService interventionService;

    @Autowired
    public EnclosServiceImpl(EnclosRepository enclosRepository,
                             @Lazy AnimalService animalService,
                             @Lazy InterventionService interventionService) {
        super(enclosRepository);
        this.animalService = animalService;
        this.interventionService = interventionService;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean enclosVide(Long id) {
        return !this.animalService.existeParEnclos(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws ServiceException {
        if (this.animalService.existeParEnclos(id)) {
            throw new ServiceException("Enclos encore occupé par des animaux");
        }
        if (this.interventionService.enclosUtilise(id)) {
            throw new ServiceException("Enclos encore rattaché à des interventions");
        }
        super.deleteById(id);
    }
}