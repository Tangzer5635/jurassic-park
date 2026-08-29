package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.dtos.PersonnelDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.PersonnelAssembler;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.services.PersonnelService;
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
@RequestMapping("/api/v1/personnels")
public class PersonnelController {

    private final PersonnelService personnelService;
    private final PersonnelAssembler personnelAssembler;

    @Autowired
    public PersonnelController(PersonnelService personnelService, PersonnelAssembler personnelAssembler) {
        this.personnelService = personnelService;
        this.personnelAssembler = personnelAssembler;
    }

    @GetMapping("/")
    public ResponseEntity<Collection<PersonnelDto>> findAll() {
        try {
            Collection<Personnel> personnels = this.personnelService.findAll(Pageable.unpaged()).getContent();
            return ResponseEntity.ok(this.personnelAssembler.toDtos(personnels));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/")
    public ResponseEntity<PersonnelDto> findById(@PathVariable Long id) {
        try {
            Optional<Personnel> optionalPersonnel = this.personnelService.findById(id);
            return optionalPersonnel
                    .map(personnelAssembler::toDto)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<PersonnelDto> create(@RequestBody PersonnelDto personnelDto) {
        try {
            if (Objects.isNull(personnelDto)) {
                return ResponseEntity.badRequest().build();
            }
            if (Objects.nonNull(personnelDto.getId())) {
                return ResponseEntity.badRequest().build();
            }
            Personnel personnel = this.personnelService.create(this.personnelAssembler.toEntity(personnelDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(this.personnelAssembler.toDto(personnel));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/")
    public ResponseEntity<PersonnelDto> update(@PathVariable Long id, @RequestBody PersonnelDto personnelDto) {
        try {
            if (Objects.isNull(personnelDto)) {
                return ResponseEntity.badRequest().build();
            }
            if (!id.equals(personnelDto.getId())) {
                return ResponseEntity.badRequest().build();
            }
            if (!this.personnelService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Personnel personnel = this.personnelService.update(this.personnelAssembler.toEntity(personnelDto));
            return ResponseEntity.ok(this.personnelAssembler.toDto(personnel));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            if (!this.personnelService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            this.personnelService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}