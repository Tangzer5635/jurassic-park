package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.dtos.AnimalDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.AnimalAssembler;
import net.ent.etnc.jurassicpark.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {

    private final AnimalService animalService;
    private final AnimalAssembler animalAssembler;

    @Autowired
    public AnimalController(AnimalService animalService, AnimalAssembler animalAssembler) {
        this.animalService = animalService;
        this.animalAssembler = animalAssembler;
    }

}