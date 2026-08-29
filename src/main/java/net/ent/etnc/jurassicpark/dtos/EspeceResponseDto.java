package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.Enclos;
import net.ent.etnc.jurassicpark.models.enumerations.Dangerosite;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEspece;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspeceResponseDto {

    private Long id;
    private String code;
    private Dangerosite dangerosite;
    private List<Enclos> encloss;
    private TypeEspece type;
}