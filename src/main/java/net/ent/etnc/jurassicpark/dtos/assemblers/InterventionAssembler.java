package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.InterventionRequestDto;
import net.ent.etnc.jurassicpark.dtos.InterventionResponseDto;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.services.AnimalService;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.PersonnelService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class InterventionAssembler {

    private final AnimalAssembler animalAssembler;
    private final PersonnelAssembler personnelAssembler;
    private final AnimalService animalService;
    private final PersonnelService personnelService;
    private final EnclosAssembler enclosAssembler;
    private final EnclosService enclosService;

    @Autowired
    public InterventionAssembler(AnimalAssembler animalAssembler, PersonnelAssembler personnelAssembler,
                                 AnimalService animalService, PersonnelService personnelService, EnclosAssembler enclosAssembler, EnclosService enclosService) {
        this.animalAssembler = animalAssembler;
        this.personnelAssembler = personnelAssembler;
        this.animalService = animalService;
        this.personnelService = personnelService;
        this.enclosAssembler = enclosAssembler;
        this.enclosService = enclosService;
    }

    public InterventionResponseDto toDto(Intervention intervention) {
        return InterventionResponseDto.builder()
                .id(intervention.getId())
                .code(intervention.getCode())
                .dateDebut(intervention.getDateDebut())
                .dateFin(intervention.getDateFin())
                .etat(intervention.getEtat())
                .type(intervention.getType())
                .animals(animalAssembler.toDtos(intervention.getAnimals()).stream().toList())
                .personnels(personnelAssembler.toDtos(intervention.getPersonnels()).stream().toList())
                .enclos(intervention.getEnclos() == null
                        ? null
                        : enclosAssembler.toDto(intervention.getEnclos()))
                .build();
    }

    public Collection<InterventionResponseDto> toDtos(Collection<Intervention> interventions) {
        return interventions.stream().map(this::toDto).toList();
    }

    public Intervention toEntity(InterventionRequestDto dto) {
        Intervention intervention = new Intervention();
        intervention.setId(dto.getId());
        intervention.setCode(dto.getCode());
        intervention.setDateDebut(dto.getDateDebut());
        intervention.setDateFin(dto.getDateFin());
        intervention.setEtat(dto.getEtat());
        intervention.setType(dto.getType());

        if (dto.getAnimalId() != null && !dto.getAnimalId().isEmpty()) {
            for (Long id : dto.getAnimalId()) {
                Optional<Animal> optionalAnimal = animalService.findById(id);
                if (optionalAnimal.isEmpty()) {
                    throw new ServiceException("Animal introuvable : " + id);
                }
                intervention.addAnimal(optionalAnimal.get());
            }
        }

        if (dto.getPersonnelId() != null && !dto.getPersonnelId().isEmpty()) {
            for (Long id : dto.getPersonnelId()) {
                Optional<Personnel> optionalPersonnel = personnelService.findById(id);
                if (optionalPersonnel.isEmpty()) {
                    throw new ServiceException("Personnel introuvable : " + id);
                }
                intervention.addPersonnel(optionalPersonnel.get());
            }
        }

        if (dto.getEnclosId() != null){
            Optional<Enclos> optionalEnclos = enclosService.findById(dto.getEnclosId());
            if (optionalEnclos.isEmpty()) {
                throw new ServiceException("Enclos introuvable : " + dto.getEnclosId());
            }
            intervention.setEnclos(optionalEnclos.get());
        }

        return intervention;
    }
}