package net.ent.etnc.jurassicpark.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import net.ent.etnc.jurassicpark.models.commons.AbstractPersistableWithIdSetter;
import net.ent.etnc.jurassicpark.models.enumerations.NiveauHabilitation;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "PERSONNEL",
        uniqueConstraints = @UniqueConstraint(name = "uk_PERSONNEL_code", columnNames = {"code"}))
@EqualsAndHashCode(callSuper = false, of = {"code"})
@ToString(callSuper = true, of = {"code", "nom", "prenom", "niveauHabiliation"})
public class Personnel extends AbstractPersistableWithIdSetter<Long> {

    @Getter
    @Setter
    @NotNull(message = "code ne doit pas être null")
    @NotEmpty(message = "code ne doit pas être vide")
    @NotBlank(message = "code doit contenir des caractères lisibles")
    @Pattern(regexp = "^\\d{10}$", message = "code doit contenir 10 chiffres")
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Getter
    @Setter
    @NotNull(message = "nom ne doit pas être null")
    @NotEmpty(message = "nom ne doit pas être vide")
    @NotBlank(message = "nom doit contenir des caractères lisibles")
    @Length(min = 3, max = 50, message = "nom doit avoir entre 3 et 50 caractères")
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @Getter
    @Setter
    @NotNull(message = "prenom ne doit pas être null")
    @NotEmpty(message = "prenom ne doit pas être vide")
    @NotBlank(message = "prenom doit contenir des caractères lisibles")
    @Length(min = 3, max = 50, message = "prenom doit avoir entre 3 et 50 caractères")
    @Column(name = "prenom", length = 50, nullable = false)
    private String prenom;
    
    @Getter
    @Setter
    @NotNull(message = "niveauHabilitation ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_habilitation", nullable = false, length = 50)
    private NiveauHabilitation niveauHabilitation;

}