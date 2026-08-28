package net.ent.etnc.jurassicpark.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import net.ent.etnc.jurassicpark.models.commons.AbstractPersistableWithIdSetter;
import net.ent.etnc.jurassicpark.models.enumerations.EtatEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.SecuriteEnclos;
import net.ent.etnc.jurassicpark.models.enumerations.TypeEnclos;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "ENCLOS",
        uniqueConstraints = @UniqueConstraint(name = "uk_ENCLOS_code", columnNames = {"code"}))
@EqualsAndHashCode(callSuper = false, of = {"code"})
@ToString(callSuper = true, of = {"code", "etat", "niveauSecurite", "type"})
public class Enclos extends AbstractPersistableWithIdSetter<Long> {

    @Getter
    @Setter
    @NotNull(message = "code ne doit pas être null")
    @NotEmpty(message = "code ne doit pas être vide")
    @NotBlank(message = "code doit contenir des caractères lisibles")
    //lettre = type enclos puis 1 chiffre correspond au niveau de sécurite et 2 chiffres = code
    @Pattern(regexp = "^[ATVQ]\\d{3}$", message = "code doit contenir 1 lettre et 3 chiffres")
    @Column(name = "code", length = 4, nullable = false)
    private String code;

    @Getter
    @Setter
    @NotNull(message = "etat ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false)
    private EtatEnclos etat;
    
    @Getter
    @Setter
    @NotNull(message = "niveauSecurite ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_securite", nullable = false)
    private SecuriteEnclos niveauSecurite;
    
    @Getter
    @Setter
    @NotNull(message = "type ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeEnclos type;
}