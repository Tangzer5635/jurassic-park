package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.repositories.InterventionRepository;
import net.ent.etnc.jurassicpark.services.InterventionService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterventionServiceImpl extends AbstractService<Intervention, InterventionRepository>
        implements InterventionService {

    @Autowired
    public InterventionServiceImpl(InterventionRepository interventionRepository) {
        super(interventionRepository);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean animalUtilise(Long animalId) {
        return this.repository.animalUtilise(animalId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean personnelUtilise(Long personnelId) {
        return this.repository.personnelUtilise(personnelId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean enclosUtilise(Long enclosId) {
        return this.repository.existsByEnclosId(enclosId);
    }
}