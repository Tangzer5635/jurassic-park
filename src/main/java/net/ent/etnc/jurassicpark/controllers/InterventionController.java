package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.dtos.assemblers.InterventionAssembler;
import net.ent.etnc.jurassicpark.services.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/interventions")
public class InterventionController {

    private final InterventionService interventionService;
    private final InterventionAssembler interventionAssembler;

    @Autowired
    public InterventionController(InterventionService interventionService, InterventionAssembler interventionAssembler) {
        this.interventionService = interventionService;
        this.interventionAssembler = interventionAssembler;
    }

}