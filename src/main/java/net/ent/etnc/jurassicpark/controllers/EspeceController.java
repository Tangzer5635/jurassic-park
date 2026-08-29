package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.controllers.commons.PageRequests;
import net.ent.etnc.jurassicpark.dtos.EspeceRequestDto;
import net.ent.etnc.jurassicpark.dtos.EspeceResponseDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.EspeceAssembler;
import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.services.EspeceService;
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
    public ResponseEntity<Page<EspeceResponseDto>> findAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        try {
            Page<Espece> especes = this.especeService.findAll(PageRequests.of(page, size, sort));
            return ResponseEntity.ok(especes.map(especeAssembler::toDto));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/{id}/")
    public ResponseEntity<EspeceResponseDto> findById(@PathVariable Long id) {
        try {
            Optional<Espece> optionalEspece = this.especeService.findById(id);
            return optionalEspece
                    .map(especeAssembler::toDto)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<EspeceResponseDto> create(@RequestBody EspeceRequestDto especeDto) {
        try {
            if (Objects.isNull(especeDto)) {
                return ResponseEntity.badRequest().build();
            }
            if (Objects.nonNull(especeDto.getId())) {
                return ResponseEntity.badRequest().build();
            }
            Espece espece = this.especeService.create(this.especeAssembler.toEntity(especeDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(this.especeAssembler.toDto(espece));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/")
    public ResponseEntity<EspeceResponseDto> update(@PathVariable Long id, @RequestBody EspeceRequestDto especeDto) {
        try {
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
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            if (!this.especeService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            this.especeService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}