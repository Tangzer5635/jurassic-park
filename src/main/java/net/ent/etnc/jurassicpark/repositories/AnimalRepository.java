package net.ent.etnc.jurassicpark.repositories;

import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.repositories.commons.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends BaseRepository<Animal> {

}