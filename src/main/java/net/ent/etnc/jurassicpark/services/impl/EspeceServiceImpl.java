package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.repositories.EspeceRepository;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.EspeceService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspeceServiceImpl extends AbstractService<Espece, EspeceRepository> implements EspeceService {

    private final AnimalService animalService;

    @Autowired
    public EspeceServiceImpl(EspeceRepository especeRepository, AnimalService animalService) {
        super(especeRepository);
        this.animalService = animalService;
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws ServiceException {
        if (this.animalService.existsByEspeceId(id)) {
            throw new ServiceException("Espèce encore rattachée à des animaux");
        }
        super.deleteById(id);
    }
}