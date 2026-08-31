package net.ent.etnc.jurassicpark.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import net.ent.etnc.jurassicpark.models.commons.AbstractPersistableWithIdSetter;
import net.ent.etnc.jurassicpark.models.enumerations.Alimentation;
import net.ent.etnc.jurassicpark.models.enumerations.Dangerosite;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEspece;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "ESPECE",
        uniqueConstraints = @UniqueConstraint(name = "uk_ESPECE_code", columnNames = {"code"}))
@EqualsAndHashCode(callSuper = false, of = {"code"})
@ToString(callSuper = true, of = {"code", "dangerosite"})
public class Espece extends AbstractPersistableWithIdSetter<Long> {
    @Getter
    @Setter
    @NotNull(message = "code ne doit pas être null")
    @NotEmpty(message = "code ne doit pas être vide")
    @NotBlank(message = "code doit contenir des caractères lisibles")
    //A= AQUATIQUE , T= TERRESTRE, V=VOLANT
    @Pattern(regexp = "^[ATV]\\d{4}", message = "Le code doit contenir une lettre puis 4 chiffres")
    @Column(name = "code", length = 5, nullable = false)
    private String code;

    @Getter
    @Setter
    @NotNull(message = "nom ne doit pas être null")
    @NotEmpty(message = "nom ne doit pas être vide")
    @NotBlank(message = "nom doit contenir des caractères lisibles")
    @Length(min = 1, max = 50, message = "nom doit avoir entre 1 et 50 caractères")
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;
    
    @Getter
    @Setter
    @NotNull(message = "dangerosite ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "dangerosite", nullable = false, length = 50)
    private Dangerosite dangerosite;

    @Getter
    @Setter
    @NotNull(message = "alimentation ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "alimentation",length = 15, nullable = false)
    private Alimentation alimentation;
    
    @Getter
    @Setter
    @NotNull(message = "type ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeEspece type;
}