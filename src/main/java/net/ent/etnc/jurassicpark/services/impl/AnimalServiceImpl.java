package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.enumerations.EtatSante;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import net.ent.etnc.jurassicpark.repositories.AnimalRepository;
import net.ent.etnc.jurassicpark.repositories.EnclosRepository;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.InterventionService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AnimalServiceImpl extends AbstractService<Animal, AnimalRepository> implements AnimalService {

    private final InterventionService interventionService;

    private final EnclosRepository enclosRepository;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository, @Lazy InterventionService interventionService, @Lazy EnclosRepository enclosRepository) {
        super(animalRepository);
        this.interventionService = interventionService;
        this.enclosRepository = enclosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean animalEstDisponible(Long idAnimal, Long idIntervention, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return this.repository.animalEstLibre(idAnimal, idIntervention, dateDebut, dateFin);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Animal> getAnimauxEnclosApresInterventionsPlanifiees(Long idEnclos) {
        Set<Animal> resultat = new HashSet<>(this.repository.findAllByEnclosId(idEnclos));
        resultat.addAll(this.repository.findAllDeplacesVers(idEnclos));
        return resultat;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeParEnclos(Long enclosId) {
        return this.repository.existsByEnclosId(enclosId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeParEspece(Long especeId) {
        return this.repository.existsByEspeceId(especeId);
    }

    @Override
    @Transactional
    public void deplacerCadavres(Set<Animal> animals) {
        Enclos cimetiere = enclosRepository.getEnclosByTypeEquals(TypeEnclos.CIMETIERE);
        for (Animal animal : animals) {
            animal.setEnclos(cimetiere);
            this.repository.save(animal);
        }
    }

    @Override
    @Transactional
    public void deplacerAnimaux(Set<Animal> animals, Enclos enclos) {
        for (Animal animal : animals) {
            animal.setEnclos(enclos);
            this.repository.save(animal);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws ServiceException {
        if (this.interventionService.animalUtilise(id)) {
            throw new ServiceException("Animal encore rattaché à des interventions");
        }
        super.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Animal> findAllByEnclosId(Long enclosId, Pageable pageable) {
        return this.repository.findAllByEnclosId(enclosId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Animal> findAllByEspeceId(Long especeId, Pageable pageable) {
        return this.repository.findAllByEspeceId(especeId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Animal> findAllByEtatSante(EtatSante etatSante, Pageable pageable) {
        return this.repository.findAllByEtatSante(etatSante, pageable);
    }
}