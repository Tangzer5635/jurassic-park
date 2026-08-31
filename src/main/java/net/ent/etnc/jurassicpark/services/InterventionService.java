package net.ent.etnc.jurassicpark.services;

import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;
import net.ent.etnc.jurassicpark.services.commons.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterventionService extends Service<Intervention, Long> {

    boolean animalUtilise(Long animalId);
    boolean personnelUtilise(Long personnelId);
    boolean enclosUtilise(Long enclosId);

    Page<Intervention> findAllByAnimalId(Long animalId, Pageable pageable);

    Page<Intervention> findAllByPersonnelId(Long personnelId, Pageable pageable);

    Page<Intervention> findAllByEnclosId(Long enclosId, Pageable pageable);

    Page<Intervention> findAllByEtat(EtatIntervention etat, Pageable pageable);

    Page<Intervention> findAllByType(TypeIntervention type, Pageable pageable);

}