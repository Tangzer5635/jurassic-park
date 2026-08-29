package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterventionRequestDto {

    private Long id;
    private String code;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private EtatIntervention etat;
    private TypeIntervention type;

    //Todo : ajouter une liste d'animals'
    private Set<Long> animalId;

    //Todo : ajouter une liste de personnels'
    private Set<Long> personnelId;
}