package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.Espece;
import net.ent.etnc.jurassicpark.models.enumerations.EtatSante;
import net.ent.etnc.jurassicpark.models.enumerations.Sexe;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDto {
    private Long id;
    private String code;
    private String prenom;
    private EtatSante etatSante;
    private Sexe sexe;
    private Espece espece;
    private Enclos enclos;
}