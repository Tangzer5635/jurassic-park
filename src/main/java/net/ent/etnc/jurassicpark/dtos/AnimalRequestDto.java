package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.enumerations.EtatSante;
import net.ent.etnc.jurassicpark.models.enumerations.Sexe;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRequestDto {
    private Long id;
    private String code;
    private String prenom;
    private EtatSante etatSante;
    private Sexe sexe;
    private Long especeId;
    private Long enclosId;
}
