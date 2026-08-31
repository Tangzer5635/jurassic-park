package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.enumerations.Alimentation;
import net.ent.etnc.jurassicpark.models.enumerations.Dangerosite;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEspece;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspeceDto {

    private Long id;
    private String code;
    private String nom;
    private Dangerosite dangerosite;
    private Alimentation alimentation;
    private TypeEspece type;
}