package net.ent.etnc.jurassicpark.services;

import jakarta.validation.constraints.NotNull;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.enumerations.EtatEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import net.ent.etnc.jurassicpark.services.commons.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface EnclosService extends Service<Enclos, Long> {

    boolean enclosVide(Long idEnclos);

    Page<Enclos> findAllByType(TypeEnclos type, Pageable pageable);

    Page<Enclos> findAllByEtat(EtatEnclos etat, Pageable pageable);

    Enclos findByAnimalId(Long id);

    Enclos getCimetiere();
}