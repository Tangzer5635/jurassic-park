package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.controllers.commons.PageRequests;
import net.ent.etnc.jurassicpark.dtos.InterventionRequestDto;
import net.ent.etnc.jurassicpark.dtos.InterventionResponseDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.InterventionAssembler;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;
import net.ent.etnc.jurassicpark.services.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    @GetMapping("/")
    public ResponseEntity<Page<InterventionResponseDto>> findAll(
            @RequestParam(required = false) Long animalId,
            @RequestParam(required = false) Long personnelId,
            @RequestParam(required = false) Long enclosId,
            @RequestParam(required = false) EtatIntervention etat,
            @RequestParam(required = false) TypeIntervention type,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        Pageable pageable = PageRequests.of(page, size, sort);

        Page<Intervention> interventions;
        if (animalId != null) {
            interventions = this.interventionService.findAllByAnimalId(animalId, pageable);
        } else if (personnelId != null) {
            interventions = this.interventionService.findAllByPersonnelId(personnelId, pageable);
        } else if (enclosId != null) {
            interventions = this.interventionService.findAllByEnclosId(enclosId, pageable);
        } else if (etat != null) {
            interventions = this.interventionService.findAllByEtat(etat, pageable);
        } else if (type != null) {
            interventions = this.interventionService.findAllByType(type, pageable);
        } else {
            interventions = this.interventionService.findAll(pageable);
        }

        return ResponseEntity.ok(interventions.map(interventionAssembler::toDto));
    }

    @GetMapping("/{id}/")
    public ResponseEntity<InterventionResponseDto> findById(@PathVariable Long id) {
        return this.interventionService.findById(id)
                .map(interventionAssembler::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/")
    public ResponseEntity<InterventionResponseDto> create(@RequestBody InterventionRequestDto interventionDto) {
        if (Objects.isNull(interventionDto)) {
            return ResponseEntity.badRequest().build();
        }
        if (Objects.nonNull(interventionDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Intervention intervention = this.interventionService.create(
                this.interventionAssembler.toEntity(interventionDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.interventionAssembler.toDto(intervention));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<InterventionResponseDto> update(@PathVariable Long id,
                                                          @RequestBody InterventionRequestDto interventionDto) {
        if (Objects.isNull(interventionDto)) {
            return ResponseEntity.badRequest().build();
        }
        if (!id.equals(interventionDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (!this.interventionService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Intervention intervention = this.interventionService.update(
                this.interventionAssembler.toEntity(interventionDto));
        return ResponseEntity.ok(this.interventionAssembler.toDto(intervention));
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!this.interventionService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        this.interventionService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}