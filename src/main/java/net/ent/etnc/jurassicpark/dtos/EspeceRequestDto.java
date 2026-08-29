package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.enumerations.Dangerosite;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEspece;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspeceRequestDto {

    private Long id;
    private String code;
    private Dangerosite dangerosite;

    //TODO : ajouter une liste d'espece'
    private Set<Long> enclosID;

    private TypeEspece type;
}