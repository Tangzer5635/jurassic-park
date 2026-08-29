package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.business.rules.AnimalDisponible;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.repositories.AnimalRepository;
import net.ent.etnc.jurassicpark.repositories.InterventionRepository;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.InterventionService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AnimalServiceImpl extends AbstractService<Animal, AnimalRepository> implements AnimalService {

    private final InterventionService interventionService;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository,
                             @Lazy InterventionService interventionService) {
        super(animalRepository);
        this.interventionService = interventionService;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean animalEstDisponible(Long id, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return this.repository.animalEstLibre(id, dateDebut, dateFin);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeParEspece(Long especeId) {
        return this.repository.existsByEspeceId(especeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeParEnclos(Long enclosId) {
        return this.repository.existsByEnclosId(enclosId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws ServiceException {
        if (this.interventionService.animalUtilise(id)) {
            throw new ServiceException("Animal encore rattaché à des interventions");
        }
        super.deleteById(id);
    }
}