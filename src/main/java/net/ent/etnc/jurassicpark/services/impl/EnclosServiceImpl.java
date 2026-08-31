package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.repositories.EnclosRepository;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EnclosServiceImpl extends AbstractService<Enclos, EnclosRepository> implements EnclosService {

    @Autowired
    public EnclosServiceImpl(EnclosRepository enclosRepository) {
        super(enclosRepository);
    }

    @Override
    public boolean enclosVide(Long id, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return false;
    }
}