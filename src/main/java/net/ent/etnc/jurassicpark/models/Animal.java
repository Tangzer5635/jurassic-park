package net.ent.etnc.jurassicpark.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import net.ent.etnc.jurassicpark.models.commons.AbstractPersistableWithIdSetter;
import net.ent.etnc.jurassicpark.models.enumerations.EtatSante;
import net.ent.etnc.jurassicpark.models.enumerations.Sexe;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "ANIMAL",
        uniqueConstraints = @UniqueConstraint(name = "uk_ANIMAL_code", columnNames = {"code"}))
@EqualsAndHashCode(callSuper = false, of = {"code"})
@ToString(callSuper = true, of = {"code", "prenom", "etatSante", "sexe"})
public class Animal extends AbstractPersistableWithIdSetter<Long> {

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
    @NotNull(message = "prenom ne doit pas être null")
    @NotEmpty(message = "prenom ne doit pas être vide")
    @NotBlank(message = "prenom doit contenir des caractères lisibles")
    @Length(min = 1, max = 50, message = "prenom doit avoir entre 1 et 50 caractères")
    @Column(name = "prenom", length = 50, nullable = false)
    private String prenom;

    @Getter
    @Setter
    @NotNull(message = "etatSante ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_sante", nullable = false, length = 50)
    private EtatSante etatSante;

    @Getter
    @Setter
    @NotNull(message = "sexe ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @Getter
    @Setter
    @NotNull(message = "l'animal doit avoir une espèce")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "espece_id", nullable = false)
    private Espece espece;

    @Getter
    @Setter
    @NotNull(message = "l'animal doit avoir un enclos")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enclos_id", nullable = false)
    private Enclos enclos;
}