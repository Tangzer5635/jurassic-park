package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.repositories.InterventionRepository;
import net.ent.etnc.jurassicpark.services.InterventionService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterventionServiceImpl extends AbstractService<Intervention, InterventionRepository> implements InterventionService {

    @Autowired
    public InterventionServiceImpl(InterventionRepository interventionRepository) {
        super(interventionRepository);
    }
}