package net.ent.etnc.jurassicpark.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import net.ent.etnc.jurassicpark.models.commons.AbstractPersistableWithIdSetter;
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
    @NotNull(message = "dangerosite ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "dangerosite", nullable = false, length = 50)
    private Dangerosite dangerosite;

    @Valid
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESPECE_id",
            foreignKey = @ForeignKey(name = "fk_ENCLOS_ESPECE"))
    private List<Enclos> encloss = new ArrayList<>();

    public List<Enclos> getEncloss() {
        return Collections.unmodifiableList(encloss);
    }

    public void addEnclos(Enclos enclos) {
        encloss.add(enclos);
    }
    public void removeEnclos(Enclos enclos) {
        encloss.remove(enclos);
    }
    
    @Getter
    @Setter
    @NotNull(message = "type ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeEspece type;
}