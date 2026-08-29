package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.dtos.EnclosDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.EnclosAssembler;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.services.EnclosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/enclos")
public class EnclosController {

    private final EnclosService enclosService;
    private final EnclosAssembler enclosAssembler;

    @Autowired
    public EnclosController(EnclosService enclosService, EnclosAssembler enclosAssembler) {
        this.enclosService = enclosService;
        this.enclosAssembler = enclosAssembler;
    }

    @GetMapping("/")
    public ResponseEntity<Collection<EnclosDto>> findAll() {
        try {
            Collection<Enclos> enclos = this.enclosService.findAll(Pageable.unpaged()).getContent();
            return ResponseEntity.ok(this.enclosAssembler.toDtos(enclos));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/")
    public ResponseEntity<EnclosDto> findById(@PathVariable Long id) {
        try {
            Optional<Enclos> optionalEnclos = this.enclosService.findById(id);
            return optionalEnclos
                    .map(enclosAssembler::toDto)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<EnclosDto> create(@RequestBody EnclosDto enclosDto) {
        try {
            if (Objects.isNull(enclosDto)) {
                return ResponseEntity.badRequest().build();
            }
            if (Objects.nonNull(enclosDto.getId())) {
                return ResponseEntity.badRequest().build();
            }
            Enclos enclos = this.enclosService.create(this.enclosAssembler.toEntity(enclosDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(this.enclosAssembler.toDto(enclos));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/")
    public ResponseEntity<EnclosDto> update(@PathVariable Long id, @RequestBody EnclosDto enclosDto) {
        try {
            if (Objects.isNull(enclosDto)) {
                return ResponseEntity.badRequest().build();
            }
            if (!id.equals(enclosDto.getId())) {
                return ResponseEntity.badRequest().build();
            }
            if (!this.enclosService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Enclos enclos = this.enclosService.update(this.enclosAssembler.toEntity(enclosDto));
            return ResponseEntity.ok(this.enclosAssembler.toDto(enclos));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            if (!this.enclosService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            this.enclosService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}