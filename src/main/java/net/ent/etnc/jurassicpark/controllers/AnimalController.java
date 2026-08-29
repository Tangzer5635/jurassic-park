package net.ent.etnc.jurassicpark.controllers;

import net.ent.etnc.jurassicpark.dtos.AnimalRequestDto;
import net.ent.etnc.jurassicpark.dtos.AnimalResponseDto;
import net.ent.etnc.jurassicpark.dtos.assemblers.AnimalAssembler;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.services.AnimalService;
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
    public ResponseEntity<Collection<AnimalResponseDto>> findAll() {
        try {
            Collection<Animal> animaux = this.animalService.findAll(Pageable.unpaged()).getContent();
            return ResponseEntity.ok(this.animalAssembler.toDtos(animaux));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/")
    public ResponseEntity<AnimalResponseDto> findById(@PathVariable Long id) {
        try {
            Optional<Animal> optionalAnimal = this.animalService.findById(id);
            return optionalAnimal
                    .map(animalAssembler::toDto)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<AnimalResponseDto> create(@RequestBody AnimalRequestDto animalDto) {
        try {
            if (Objects.isNull(animalDto)) {
                return ResponseEntity.badRequest().build();
            }
            if (Objects.nonNull(animalDto.getId())) {
                return ResponseEntity.badRequest().build();
            }
            Animal animal = this.animalService.create(this.animalAssembler.toEntity(animalDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(this.animalAssembler.toDto(animal));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/")
    public ResponseEntity<AnimalResponseDto> update(@PathVariable Long id, @RequestBody AnimalRequestDto animalDto) {
        try {
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
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            if (!this.animalService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            this.animalService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}