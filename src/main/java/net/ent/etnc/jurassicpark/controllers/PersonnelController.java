package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.controllers.commons.PageRequests;
import net.ent.etnc.jurassicpark.dtos.PersonnelDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.PersonnelAssembler;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.models.enumerations.NiveauHabilitation;
import net.ent.etnc.jurassicpark.services.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public ResponseEntity<Page<PersonnelDto>> findAll(
            @RequestParam(required = false) NiveauHabilitation niveauHabilitation,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        Pageable pageable = PageRequests.of(page, size, sort);

        Page<Personnel> personnels = niveauHabilitation != null
                ? this.personnelService.findAllByNiveauHabilitation(niveauHabilitation, pageable)
                : this.personnelService.findAll(pageable);

        return ResponseEntity.ok(personnels.map(personnelAssembler::toDto));
    }

    @GetMapping("/{id}/")
    public ResponseEntity<PersonnelDto> findById(@PathVariable Long id) {
        return this.personnelService.findById(id)
                .map(personnelAssembler::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/")
    public ResponseEntity<PersonnelDto> create(@RequestBody PersonnelDto personnelDto) {
        if (Objects.isNull(personnelDto)) {
            return ResponseEntity.badRequest().build();
        }
        if (Objects.nonNull(personnelDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Personnel personnel = this.personnelService.create(this.personnelAssembler.toEntity(personnelDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.personnelAssembler.toDto(personnel));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<PersonnelDto> update(@PathVariable Long id, @RequestBody PersonnelDto personnelDto) {
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
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!this.personnelService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        this.personnelService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}