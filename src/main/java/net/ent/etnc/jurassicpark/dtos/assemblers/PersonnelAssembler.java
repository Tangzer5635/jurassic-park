package net.ent.etnc.jurassicpark.dtos.assemblers;

import net.ent.etnc.jurassicpark.dtos.PersonnelDto;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.services.PersonnelService;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonnelAssembler {

    private final PersonnelService personnelService;

    @Autowired
    public PersonnelAssembler(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

}