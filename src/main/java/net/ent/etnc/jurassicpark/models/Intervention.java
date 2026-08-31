package net.ent.etnc.jurassicpark.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import net.ent.etnc.jurassicpark.models.commons.AbstractPersistableWithIdSetter;
import net.ent.etnc.jurassicpark.models.enumerations.EtatIntervention;
import net.ent.etnc.jurassicpark.models.enumerations.TypeIntervention;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "INTERVENTION",
        uniqueConstraints = @UniqueConstraint(name = "uk_INTERVENTION_code", columnNames = {"code"}))
@EqualsAndHashCode(callSuper = false, of = {"code"})
@ToString(callSuper = true, of = {"code", "dateDebut", "dateFin", "type", "etat"})
public class Intervention extends AbstractPersistableWithIdSetter<Long> {
    @Getter
    @Setter
    @NotNull(message = "code ne doit pas être null")
    @NotEmpty(message = "code ne doit pas être vide")
    @NotBlank(message = "code doit contenir des caractères lisibles")
    //F = NOURRISSAGE, N = NETTOYAGE, S= SURVEILLANCE, D= DÉPLACEMENT, M= SOIN_MÉDICAUX, C= CAPTURE_URGENTE
    @Pattern(regexp = "^[FNSDMCE]\\d{9}$", message = "Le code doit contenir une lettre puis 9 chiffres")
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Valid
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "INTERVENTION_PERSONNEL",
            joinColumns = @JoinColumn(name = "intervention_id",
                    foreignKey = @ForeignKey(name = "fk_INTERVENTION_PERSONNEL_intervention")),
            inverseJoinColumns = @JoinColumn(name = "personnel_id",
                    foreignKey = @ForeignKey(name = "fk_INTERVENTION_PERSONNEL_personnel")))
    private Set<Personnel> personnels =  new HashSet<>();

    public Set<Personnel> getPersonnels() {
        return Collections.unmodifiableSet(personnels);
    }

    public void addPersonnel(Personnel personnel) {
        personnels.add(personnel);
    }
    public void removePersonnel(Personnel personnel) {
        personnels.remove(personnel);
    }

    @Valid
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "INTERVENTION_ANIMAL",
            joinColumns = @JoinColumn(name = "intervention_id",
                    foreignKey = @ForeignKey(name = "fk_INTERVENTION_ANIMAL_intervention")),
            inverseJoinColumns = @JoinColumn(name = "animal_id",
                    foreignKey = @ForeignKey(name = "fk_INTERVENTION_ANIMAL_animal")))
    private Set<Animal> animals =  new HashSet<>();

    public Set<Animal> getAnimals() {
        return Collections.unmodifiableSet(animals);
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    @Getter
    @Setter
    @NotNull(message = "dateDebut ne doit pas être null")
    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Getter
    @Setter
    @NotNull(message = "dateFin ne doit pas être null")
    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @AssertTrue(message = "dateFin doit être postérieure à dateDebut")
    public boolean isPeriodeValide() {
        return dateDebut == null || dateFin == null || dateFin.isAfter(dateDebut);
    }

    @Getter
    @Setter
    @NotNull(message = "etat ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "etat", nullable = false, length = 50)
    private EtatIntervention etat;
    
    @Getter
    @Setter
    @NotNull(message = "type ne doit pas être null")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private TypeIntervention type;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enclos_id", foreignKey = @ForeignKey(name = "fk_INTERVENTION_ENCLOS"))
    private Enclos enclos;


}