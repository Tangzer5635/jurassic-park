package net.ent.etnc.jurassicpark.repositories;

import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.enumerations.EtatEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import net.ent.etnc.jurassicpark.repositories.commons.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface EnclosRepository extends BaseRepository<Enclos> {

    Enclos getEnclosByTypeEquals(TypeEnclos type);

    Page<Enclos> findAllByType(TypeEnclos type, Pageable pageable);

    Page<Enclos> findAllByEtat(EtatEnclos etat, Pageable pageable);
}