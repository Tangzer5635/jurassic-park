package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.controllers.commons.PageRequests;
import net.ent.etnc.jurassicpark.dtos.EnclosDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.EnclosAssembler;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.enumerations.EtatEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import net.ent.etnc.jurassicpark.services.EnclosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public ResponseEntity<Page<EnclosDto>> findAll(
            @RequestParam(required = false) TypeEnclos type,
            @RequestParam(required = false) EtatEnclos etat,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        Pageable pageable = PageRequests.of(page, size, sort);

        Page<Enclos> enclos;
        if (type != null) {
            enclos = this.enclosService.findAllByType(type, pageable);
        } else if (etat != null) {
            enclos = this.enclosService.findAllByEtat(etat, pageable);
        } else {
            enclos = this.enclosService.findAll(pageable);
        }

        return ResponseEntity.ok(enclos.map(enclosAssembler::toDto));
    }

    @GetMapping("/{id}/")
    public ResponseEntity<EnclosDto> findById(@PathVariable Long id) {
        return this.enclosService.findById(id)
                .map(enclosAssembler::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/")
    public ResponseEntity<EnclosDto> create(@RequestBody EnclosDto enclosDto) {
        if (Objects.isNull(enclosDto)) {
            return ResponseEntity.badRequest().build();
        }
        if (Objects.nonNull(enclosDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Enclos enclos = this.enclosService.create(this.enclosAssembler.toEntity(enclosDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.enclosAssembler.toDto(enclos));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<EnclosDto> update(@PathVariable Long id, @RequestBody EnclosDto enclosDto) {
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
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!this.enclosService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        this.enclosService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}