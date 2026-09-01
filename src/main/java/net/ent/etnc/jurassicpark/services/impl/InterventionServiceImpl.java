package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.business.validators.InterventionValidator;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;
import net.ent.etnc.jurassicpark.repositories.InterventionRepository;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.InterventionService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class InterventionServiceImpl extends AbstractService<Intervention, InterventionRepository>
        implements InterventionService {

    private final InterventionValidator validator;
    private final AnimalService animalService;

    @Autowired
    public InterventionServiceImpl(InterventionRepository interventionRepository,
                                   InterventionValidator validator, AnimalService animalService) {
        super(interventionRepository);
        this.validator = validator;
        this.animalService = animalService;
    }

    @Override
    @Transactional
    public Intervention create(Intervention intervention) throws ServiceException {
        controler(intervention);

        if (intervention.getEtat() == EtatIntervention.TERMINEE) {
            effectuerIntervention(intervention);
        }

        return super.create(intervention);
    }

    @Override
    @Transactional
    public Intervention update(Intervention intervention) throws ServiceException {
        EtatIntervention actuel = this.repository.findById(intervention.getId())
                .orElseThrow(() -> new ServiceException("Intervention introuvable"))
                .getEtat();

        if (actuel == EtatIntervention.TERMINEE || actuel == EtatIntervention.ANNULEE) {
            throw new ServiceException("Intervention terminée ou annulée, non modifiable");
        }
        if (!actuel.peutDevenir(intervention.getEtat())) {
            throw new ServiceException("Transition interdite : " + actuel + " vers " + intervention.getEtat());
        }
        controler(intervention);

        if (intervention.getEtat() == EtatIntervention.TERMINES) {
            effectuerIntervention(intervention);
        }

        return super.update(intervention);
    }

    private void controler(Intervention intervention) throws ServiceException {
        ResultatValidation resultat = this.validator.valider(intervention);
        if (!resultat.estValide()) {
            throw new ServiceException(resultat.erreurs().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", ")));
        }
    }

    private void effectuerIntervention(Intervention intervention) {
        switch (intervention.getType()) {
            case DEPLACEMENT -> animalService.deplacerAnimaux(intervention.getAnimals(), intervention.getEnclos());
            case EQUARRISSAGE -> animalService.deplacerCadavres(intervention.getAnimals());
        }
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

    @Override
    @Transactional(readOnly = true)
    public Page<Intervention> findAllByAnimalId(Long animalId, Pageable pageable) {
        return this.repository.findAllByAnimalId(animalId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Intervention> findAllByPersonnelId(Long personnelId, Pageable pageable) {
        return this.repository.findAllByPersonnelId(personnelId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Intervention> findAllByEnclosId(Long enclosId, Pageable pageable) {
        return this.repository.findAllByEnclosId(enclosId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Intervention> findAllByEtat(EtatIntervention etat, Pageable pageable) {
        return this.repository.findAllByEtat(etat, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Intervention> findAllByType(TypeIntervention type, Pageable pageable) {
        return this.repository.findAllByType(type, pageable);
    }
}