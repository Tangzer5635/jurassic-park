package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.controllers.commons.PageRequests;
import net.ent.etnc.jurassicpark.dtos.InterventionRequestDto;
import net.ent.etnc.jurassicpark.dtos.InterventionResponseDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.InterventionAssembler;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.services.InterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

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
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        try {
            Page<Intervention> interventions = this.interventionService.findAll(PageRequests.of(page, size, sort));
            return ResponseEntity.ok(interventions.map(interventionAssembler::toDto));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/")
    public ResponseEntity<InterventionResponseDto> findById(@PathVariable Long id) {
        try {
            Optional<Intervention> optionalIntervention = this.interventionService.findById(id);
            return optionalIntervention
                    .map(interventionAssembler::toDto)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<InterventionResponseDto> create(@RequestBody InterventionRequestDto interventionDto) {
        try {
            if (Objects.isNull(interventionDto)) {
                return ResponseEntity.badRequest().build();
            }
            if (Objects.nonNull(interventionDto.getId())) {
                return ResponseEntity.badRequest().build();
            }
            Intervention intervention = this.interventionService.create(
                    this.interventionAssembler.toEntity(interventionDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(this.interventionAssembler.toDto(intervention));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/")
    public ResponseEntity<InterventionResponseDto> update(@PathVariable Long id,
                                                          @RequestBody InterventionRequestDto interventionDto) {
        try {
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
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            if (!this.interventionService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            this.interventionService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}