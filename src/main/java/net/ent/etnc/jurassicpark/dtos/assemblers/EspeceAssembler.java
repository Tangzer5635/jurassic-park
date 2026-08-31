package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.EspeceDto;
import net.ent.etnc.jurassicpark.models.Espece;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class EspeceAssembler {

    public EspeceDto toDto(Espece espece) {
        return EspeceDto.builder()
                .id(espece.getId())
                .code(espece.getCode())
                .nom(espece.getNom())
                .dangerosite(espece.getDangerosite())
                .alimentation(espece.getAlimentation())
                .type(espece.getType())
                .build();
    }

    public Collection<EspeceDto> toDtos(Collection<Espece> especes) {
        return especes.stream().map(this::toDto).toList();
    }

    public Espece toEntity(EspeceDto dto) {
        Espece espece = new Espece();
        espece.setId(dto.getId());
        espece.setCode(dto.getCode());
        espece.setNom(dto.getNom());
        espece.setDangerosite(dto.getDangerosite());
        espece.setAlimentation(dto.getAlimentation());
        espece.setType(dto.getType());

        return espece;
    }
}