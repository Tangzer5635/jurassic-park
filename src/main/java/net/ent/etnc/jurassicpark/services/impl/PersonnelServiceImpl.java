package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.models.enumerations.NiveauHabilitation;
import net.ent.etnc.jurassicpark.repositories.PersonnelRepository;
import net.ent.etnc.jurassicpark.services.InterventionService;
import net.ent.etnc.jurassicpark.services.PersonnelService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PersonnelServiceImpl extends AbstractService<Personnel, PersonnelRepository> implements PersonnelService {

    private final InterventionService interventionService;

    @Autowired
    public PersonnelServiceImpl(PersonnelRepository personnelRepository,
                                @Lazy InterventionService interventionService) {
        super(personnelRepository);
        this.interventionService = interventionService;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean personnelEstDisponible(Long id, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return this.repository.personnelEstLibre(id, dateDebut, dateFin);
    }

    @Override
    @Transactional
    public void deleteById(Long id) throws ServiceException {
        if (this.interventionService.personnelUtilise(id)) {
            throw new ServiceException("Personnel encore rattaché à des interventions");
        }
        super.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Personnel> findAllByNiveauHabilitation(NiveauHabilitation niveau, Pageable pageable) {
        return this.repository.findAllByNiveauHabilitation(niveau, pageable);
    }
}