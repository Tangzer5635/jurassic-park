package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.dtos.PersonnelDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.PersonnelAssembler;
import net.ent.etnc.jurassicpark.services.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/personnels")
public class PersonnelController {

    private final PersonnelService personnelService;
    private final PersonnelAssembler personnelAssembler;

    @Autowired
    public PersonnelController(PersonnelService personnelService, PersonnelAssembler personnelAssembler) {
        this.personnelService = personnelService;
        this.personnelAssembler = personnelAssembler;
    }

}