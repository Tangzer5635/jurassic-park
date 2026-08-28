package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.repositories.EspeceRepository;
import net.ent.etnc.jurassicpark.services.EspeceService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspeceServiceImpl extends AbstractService<Espece, EspeceRepository> implements EspeceService {

    @Autowired
    public EspeceServiceImpl(EspeceRepository especeRepository) {
        super(especeRepository);
    }
}