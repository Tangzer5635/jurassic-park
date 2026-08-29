package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.AnimalRequestDto;
import net.ent.etnc.jurassicpark.dtos.AnimalResponseDto;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.EspeceService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class AnimalAssembler {

    private final EspeceAssembler especeAssembler;
    private final EnclosAssembler enclosAssembler;
    private final EspeceService especeService;
    private final EnclosService enclosService;

    @Autowired
    public AnimalAssembler(EspeceAssembler especeAssembler, EnclosAssembler enclosAssembler,
                           EspeceService especeService, EnclosService enclosService) {
        this.especeAssembler = especeAssembler;
        this.enclosAssembler = enclosAssembler;
        this.especeService = especeService;
        this.enclosService = enclosService;
    }

    public AnimalResponseDto toDto(Animal animal) {
        return AnimalResponseDto.builder()
                .id(animal.getId())
                .code(animal.getCode())
                .prenom(animal.getPrenom())
                .etatSante(animal.getEtatSante())
                .sexe(animal.getSexe())
                .espece(especeAssembler.toDto(animal.getEspece()))
                .enclos(enclosAssembler.toDto(animal.getEnclos()))
                .build();
    }

    public Collection<AnimalResponseDto> toDtos(Collection<Animal> animals) {
        return animals.stream().map(this::toDto).toList();
    }

    public Animal toEntity(AnimalRequestDto dto) {
        Animal animal = new Animal();
        animal.setId(dto.getId());
        animal.setCode(dto.getCode());
        animal.setPrenom(dto.getPrenom());
        animal.setEtatSante(dto.getEtatSante());
        animal.setSexe(dto.getSexe());

        Optional<Espece> optionalEspece = especeService.findById(dto.getEspeceId());
        if (optionalEspece.isEmpty()) {
            throw new ServiceException("Espèce introuvable : " + dto.getEspeceId());
        }
        animal.setEspece(optionalEspece.get());

        Optional<Enclos> optionalEnclos = enclosService.findById(dto.getEnclosId());
        if (optionalEnclos.isEmpty()) {
            throw new ServiceException("Enclos introuvable : " + dto.getEnclosId());
        }
        animal.setEnclos(optionalEnclos.get());

        return animal;
    }
}