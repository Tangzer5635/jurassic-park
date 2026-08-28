package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.dtos.EnclosDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.EnclosAssembler;
import net.ent.etnc.jurassicpark.services.EnclosService;
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
@RequestMapping("/api/v1/encloss")
public class EnclosController {

    private final EnclosService enclosService;
    private final EnclosAssembler enclosAssembler;

    @Autowired
    public EnclosController(EnclosService enclosService, EnclosAssembler enclosAssembler) {
        this.enclosService = enclosService;
        this.enclosAssembler = enclosAssembler;
    }

}