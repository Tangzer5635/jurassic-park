package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.PersonnelDto;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.services.PersonnelService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PersonnelAssembler {

    public PersonnelDto toDto(Personnel personnel) {
        return PersonnelDto.builder()
                .id(personnel.getId())
                .code(personnel.getCode())
                .nom(personnel.getNom())
                .prenom(personnel.getPrenom())
                .niveauHabilitation(personnel.getNiveauHabilitation())
                .build();
    }

    public Collection<PersonnelDto> toDtos(Collection<Personnel> personnels) {
        return personnels.stream().map(this::toDto).toList();
    }

    public Personnel toEntity(PersonnelDto personnelDto) throws ServiceException {
        try {
            Personnel personnel = new Personnel();
            personnel.setId(personnelDto.getId());
            personnel.setCode(personnelDto.getCode());
            personnel.setNom(personnelDto.getNom());
            personnel.setPrenom(personnelDto.getPrenom());
            return personnel;
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

}