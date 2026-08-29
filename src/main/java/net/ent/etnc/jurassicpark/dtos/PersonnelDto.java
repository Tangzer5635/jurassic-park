package net.ent.etnc.jurassicpark.dtos;

import lombok.*;
import net.ent.etnc.jurassicpark.models.enumerations.NiveauHabilitation;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelDto {

    private Long id;
    private String code;
    private String nom;
    private String prenom;
    private NiveauHabilitation niveauHabilitation;
}