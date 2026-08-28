package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.dtos.EspeceDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.EspeceAssembler;
import net.ent.etnc.jurassicpark.services.EspeceService;
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
@RequestMapping("/api/v1/especes")
public class EspeceController {

    private final EspeceService especeService;
    private final EspeceAssembler especeAssembler;

    @Autowired
    public EspeceController(EspeceService especeService, EspeceAssembler especeAssembler) {
        this.especeService = especeService;
        this.especeAssembler = especeAssembler;
    }

}