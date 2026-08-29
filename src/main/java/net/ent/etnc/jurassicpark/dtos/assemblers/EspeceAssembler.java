package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.EspeceRequestDto;
import net.ent.etnc.jurassicpark.dtos.EspeceResponseDto;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class EspeceAssembler {

    private final EnclosAssembler enclosAssembler;
    private final EnclosService enclosService;

    @Autowired
    public EspeceAssembler(EnclosAssembler enclosAssembler, EnclosService enclosService) {
        this.enclosAssembler = enclosAssembler;
        this.enclosService = enclosService;
    }

    public EspeceResponseDto toDto(Espece espece) {
        return EspeceResponseDto.builder()
                .id(espece.getId())
                .code(espece.getCode())
                .nom(espece.getNom())
                .dangerosite(espece.getDangerosite())
                .alimentation(espece.getAlimentation())
                .type(espece.getType())
                .enclos(enclosAssembler.toDtos(espece.getEnclos()).stream().toList())
                .build();
    }

    public Collection<EspeceResponseDto> toDtos(Collection<Espece> especes) {
        return especes.stream().map(this::toDto).toList();
    }

    public Espece toEntity(EspeceRequestDto dto) {
        Espece espece = new Espece();
        espece.setId(dto.getId());
        espece.setCode(dto.getCode());
        espece.setNom(dto.getNom());
        espece.setDangerosite(dto.getDangerosite());
        espece.setAlimentation(dto.getAlimentation());
        espece.setType(dto.getType());

        if (dto.getEnclosId() != null && !dto.getEnclosId().isEmpty()) {
            for (Long id : dto.getEnclosId()) {
                Optional<Enclos> optionalEnclos = enclosService.findById(id);
                if (optionalEnclos.isEmpty()) {
                    throw new ServiceException("Enclos introuvable avec l'ID " + id);
                }
                espece.addEnclos(optionalEnclos.get());
            }
        }

        return espece;
    }
}