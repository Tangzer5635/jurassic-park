package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.controllers.commons.PageRequests;
import net.ent.etnc.jurassicpark.dtos.EspeceDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.EspeceAssembler;
import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.services.EspeceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    @GetMapping("/")
    public ResponseEntity<Page<EspeceDto>> findAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        Page<Espece> especes = this.especeService.findAll(PageRequests.of(page, size, sort));
        return ResponseEntity.ok(especes.map(especeAssembler::toDto));
    }

    @GetMapping("/{id}/")
    public ResponseEntity<EspeceDto> findById(@PathVariable Long id) {
        return this.especeService.findById(id)
                .map(especeAssembler::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/")
    public ResponseEntity<EspeceDto> create(@RequestBody EspeceDto especeDto) {
        if (Objects.isNull(especeDto)) {
            return ResponseEntity.badRequest().build();
        }
        if (Objects.nonNull(especeDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Espece espece = this.especeService.create(this.especeAssembler.toEntity(especeDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.especeAssembler.toDto(espece));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<EspeceDto> update(@PathVariable Long id, @RequestBody EspeceDto especeDto) {
        if (Objects.isNull(especeDto)) {
            return ResponseEntity.badRequest().build();
        }
        if (!id.equals(especeDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (!this.especeService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Espece espece = this.especeService.update(this.especeAssembler.toEntity(especeDto));
        return ResponseEntity.ok(this.especeAssembler.toDto(espece));
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!this.especeService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        this.especeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}