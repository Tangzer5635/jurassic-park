package net.ent.etnc.jurassicpark.services.impl;

import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.repositories.AnimalRepository;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.commons.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AnimalServiceImpl extends AbstractService<Animal, AnimalRepository> implements AnimalService {

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository) {
        super(animalRepository);
    }

    @Override
    public boolean animalEstDisponible(Long id, LocalDateTime dateDebut, LocalDateTime dateFin) {

        // dans le service intervention, récupérer toutes les interventions de cet animal qui ne sont pas finis avant le début de celle-ci

        // vérifier que les autres interventions ne commencent pas avant la fin de celle-ci

        return false;
    }

    @Override
    public Set<Animal> getAnimauxEnclosApresInterventionsPlanifiees(Long idEnclos) {

        //récupérer les animaux présents dans l'enclos

        //récupérer les déplacements vers cet enclos

        //ajouter tous les animaux déplacés vers cet enclos dans le set

        return null;
    }
}