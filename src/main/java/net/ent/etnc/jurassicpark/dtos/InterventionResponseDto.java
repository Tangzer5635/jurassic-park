package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.Animal;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterventionResponseDto {
    private Long id;
    private String code;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private EtatIntervention etat;
    private TypeIntervention type;
    private List<AnimalResponseDto> animals;
    private List<PersonnelDto> personnels;
}
