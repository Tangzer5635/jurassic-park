package net.ent.etnc.jurassicpark.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.ent.etnc.jurassicpark.models.commons.AbstractPersistableWithIdSetter;

@Entity
@Table(name = "ENCLOS",
        uniqueConstraints = @UniqueConstraint(name = "uk_ENCLOS_nom", columnNames = {"nom"}))
@EqualsAndHashCode(callSuper = false, of = {"nom"})
@ToString(callSuper = true, of = {"nom"})
public class Enclos extends AbstractPersistableWithIdSetter<Long> {

}