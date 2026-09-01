package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.enumerations.EtatEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import net.ent.etnc.jurassicpark.repositories.EnclosRepository;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EnclosServiceImpl extends AbstractService<Enclos, EnclosRepository> implements EnclosService {

    private final AnimalService animalService;

    @Autowired
    public EnclosServiceImpl(EnclosRepository enclosRepository, AnimalService animalService) {
        super(enclosRepository);
        this.animalService = animalService;
    }


    @Override
    @Transactional
    public Enclos create(Enclos enclos) {
        try {

            if ((enclos.getType() == TypeEnclos.CIMETIERE) && repository.existsByType(TypeEnclos.CIMETIERE)) throw new ServiceException("il ne peut y avoir 2 cimetières");

            return repository.save(enclos);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la sauvegarde", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean enclosVide(Long idEnclos) {
        return this.repository.enclosEstVide(idEnclos);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Enclos> findAllByType(TypeEnclos type, Pageable pageable) {
        return this.repository.findAllByType(type, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Enclos> findAllByEtat(EtatEnclos etat, Pageable pageable) {
        return this.repository.findAllByEtat(etat, pageable);
    }

}