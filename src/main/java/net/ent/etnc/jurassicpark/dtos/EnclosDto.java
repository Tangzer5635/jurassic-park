package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.enumerations.EtatEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.SecuriteEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnclosDto {

    private Long id;
    private String code;
    private EtatEnclos etat;
    private SecuriteEnclos niveauSecurite;
    private TypeEnclos type;
    private Integer capaciteMax;
}