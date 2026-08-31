package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.controllers.commons.PageRequests;
import net.ent.etnc.jurassicpark.dtos.AnimalRequestDto;
import net.ent.etnc.jurassicpark.dtos.AnimalResponseDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.AnimalAssembler;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.enumerations.EtatSante;
import net.ent.etnc.jurassicpark.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/animaux")
public class AnimalController {

    private final AnimalService animalService;
    private final AnimalAssembler animalAssembler;

    @Autowired
    public AnimalController(AnimalService animalService, AnimalAssembler animalAssembler) {
        this.animalService = animalService;
        this.animalAssembler = animalAssembler;
    }

    @GetMapping("/")
    public ResponseEntity<Page<AnimalResponseDto>> findAll(
            @RequestParam(required = false) Long enclosId,
            @RequestParam(required = false) Long especeId,
            @RequestParam(required = false) EtatSante etatSante,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        Pageable pageable = PageRequests.of(page, size, sort);

        Page<Animal> animaux;
        if (enclosId != null) {
            animaux = this.animalService.findAllByEnclosId(enclosId, pageable);
        } else if (especeId != null) {
            animaux = this.animalService.findAllByEspeceId(especeId, pageable);
        } else if (etatSante != null) {
            animaux = this.animalService.findAllByEtatSante(etatSante, pageable);
        } else {
            animaux = this.animalService.findAll(pageable);
        }

        return ResponseEntity.ok(animaux.map(animalAssembler::toDto));
    }

    @GetMapping("/{id}/")
    public ResponseEntity<AnimalResponseDto> findById(@PathVariable Long id) {
        return this.animalService.findById(id)
                .map(animalAssembler::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/")
    public ResponseEntity<AnimalResponseDto> create(@RequestBody AnimalRequestDto animalDto) {
        if (Objects.isNull(animalDto)) {
            return ResponseEntity.badRequest().build();
        }
        if (Objects.nonNull(animalDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Animal animal = this.animalService.create(this.animalAssembler.toEntity(animalDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.animalAssembler.toDto(animal));
    }

    @PutMapping("/{id}/")
    public ResponseEntity<AnimalResponseDto> update(@PathVariable Long id, @RequestBody AnimalRequestDto animalDto) {
        if (Objects.isNull(animalDto)) {
            return ResponseEntity.badRequest().build();
        }
        if (!id.equals(animalDto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (!this.animalService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Animal animal = this.animalService.update(this.animalAssembler.toEntity(animalDto));
        return ResponseEntity.ok(this.animalAssembler.toDto(animal));
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!this.animalService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        this.animalService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}