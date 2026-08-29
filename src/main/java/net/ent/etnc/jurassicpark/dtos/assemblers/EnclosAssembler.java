package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.EnclosDto;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.services.EnclosService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class EnclosAssembler {

    public EnclosDto toDto(Enclos enclos) {
        return EnclosDto.builder()
                .id(enclos.getId())
                .code(enclos.getCode())
                .etat(enclos.getEtat())
                .niveauSecurite(enclos.getNiveauSecurite())
                .type(enclos.getType())
                .build();
    }

    public Collection<EnclosDto> toDtos(Collection<Enclos> enclos) {
        return enclos.stream().map(this::toDto).toList();
    }

    public Enclos toEntity(EnclosDto enclosDto) throws ServiceException {
        try {
            Enclos enclos = new Enclos();
            enclos.setId(enclosDto.getId());
            enclos.setCode(enclosDto.getCode());
            enclos.setEtat(enclosDto.getEtat());
            enclos.setNiveauSecurite(enclosDto.getNiveauSecurite());
            enclos.setType(enclosDto.getType());
            return enclos;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

}