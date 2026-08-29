package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.dtos.assemblers.EspeceAssembler;
import net.ent.etnc.jurassicpark.services.EspeceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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