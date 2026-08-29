package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.enumerations.Alimentation;
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
    private Alimentation alimentation;
    private Set<Long> enclosId;
    private TypeEspece type;
}