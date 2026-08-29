package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.repositories.PersonnelRepository;
import net.ent.etnc.jurassicpark.services.PersonnelService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PersonnelServiceImpl extends AbstractService<Personnel, PersonnelRepository> implements PersonnelService {

    @Autowired
    public PersonnelServiceImpl(PersonnelRepository personnelRepository) {
        super(personnelRepository);
    }

    @Override
    public boolean personnelEstDisponnible(Long id, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return false;
    }
}