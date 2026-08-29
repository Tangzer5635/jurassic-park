package net.ent.etnc.jurassicpark.init;

import com.github.javafaker.Faker;
import net.ent.etnc.jurassicpark.models.*;
import net.ent.etnc.jurassicpark.models.enumerations.*;
import net.ent.etnc.jurassicpark.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class Init implements CommandLineRunner {

    private final Faker faker = new Faker(new java.util.Locale("fr"));

    private final AnimalService animalService;
    private final EnclosService enclosService;
    private final EspeceService especeService;
    private final InterventionService interventionService;
    private final PersonnelService personnelService;

    // Références conservées entre les étapes : évite de deviner les IDs générés
    private final Map<NomEspece, List<Enclos>> enclosParEspece = new EnumMap<>(NomEspece.class);
    private final Map<NomEspece, Espece> especes = new EnumMap<>(NomEspece.class);
    private final List<Enclos> enclosQuarantaine = new ArrayList<>();
    private final List<Animal> animaux = new ArrayList<>();
    private final List<Personnel> personnels = new ArrayList<>();

    private int compteurEnclos = 0;

    @Autowired
    public Init(AnimalService animalService, EnclosService enclosService, EspeceService especeService,
                InterventionService interventionService, PersonnelService personnelService) {
        this.animalService = animalService;
        this.enclosService = enclosService;
        this.especeService = especeService;
        this.interventionService = interventionService;
        this.personnelService = personnelService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.especeService.count() > 0) {
            return;
        }
        this.chargerEnclos();
        this.chargeEspeces();
        this.chargeAnimaux();
        this.chargePersonnels();
        this.chargeInterventions();
    }

    private <T extends Enum<T>> T alea(T[] valeurs) {
        return valeurs[faker.number().numberBetween(0, valeurs.length)];
    }

    private char lettre(TypeEnclos type) {
        return switch (type) {
            case AQUATIQUE -> 'A';
            case TERRESTRE -> 'T';
            case VOLIERE -> 'V';
            case QUARANTAINE -> 'Q';
        };
    }

    private char lettre(TypeEspece type) {
        return switch (type) {
            case AQUATIQUE -> 'A';
            case TERRESTRE -> 'T';
            case VOLANT -> 'V';
        };
    }

    private char lettre(TypeIntervention type) {
        return switch (type) {
            case NOURRISSAGE -> 'F';
            case NETTOYAGE -> 'N';
            case SURVEILLANCE -> 'S';
            case DEPLACEMENT -> 'D';
            case SOIN_MEDICAL -> 'M';
            case CAPTURE_URGENTE -> 'C';
        };
    }

    /** Un Velociraptor va en enclos TERRESTRE, un Pteranodon en VOLIERE. */
    private TypeEnclos enclosPour(TypeEspece type) {
        return switch (type) {
            case AQUATIQUE -> TypeEnclos.AQUATIQUE;
            case TERRESTRE -> TypeEnclos.TERRESTRE;
            case VOLANT -> TypeEnclos.VOLIERE;
        };
    }

    private Enclos creerEnclos(TypeEnclos type, SecuriteEnclos securite) {
        Enclos enclos = new Enclos();
        enclos.setType(type);
        enclos.setNiveauSecurite(securite);
        enclos.setEtat(EtatEnclos.ACTIF);
        // lettre = type, 1 chiffre = niveau de sécurité, 2 chiffres = numéro
        enclos.setCode("" + lettre(type)
                + (securite.getSecuriteEnclos() / 10)
                + String.format("%02d", compteurEnclos++));
        return this.enclosService.create(enclos);
    }

    private void chargerEnclos() {
        // 2 enclos par espèce, au type et au niveau de sécurité qu'elle exige
        for (NomEspece nom : NomEspece.values()) {
            TypeEnclos type = enclosPour(nom.getType());
            SecuriteEnclos securite = nom.getDangerosite().getSecuriteMinimaleRequise();

            List<Enclos> lot = new ArrayList<>();
            lot.add(creerEnclos(type, securite));
            lot.add(creerEnclos(type, securite));
            this.enclosParEspece.put(nom, lot);
        }

        // Quarantaine : sécurité maximale, rattachée à aucune espèce
        for (int i = 0; i < 3; i++) {
            this.enclosQuarantaine.add(creerEnclos(TypeEnclos.QUARANTAINE, SecuriteEnclos.MAXIMUM));
        }
    }

    private void chargeEspeces() {
        int i = 0;
        for (NomEspece nom : NomEspece.values()) {
            Espece espece = new Espece();
            espece.setNom(nom.getLibelle());
            espece.setType(nom.getType());
            espece.setAlimentation(nom.getAlimentation());
            espece.setDangerosite(nom.getDangerosite());
            espece.setCode(lettre(nom.getType()) + String.format("%04d", i++));
            this.enclosParEspece.get(nom).forEach(espece::addEnclos);

            this.especes.put(nom, this.especeService.create(espece));
        }
    }

    private void chargeAnimaux() {
        int compteur = 0;
        for (NomEspece nom : NomEspece.values()) {
            List<Enclos> disponibles = this.enclosParEspece.get(nom);

            for (int j = 0; j < 2; j++) {
                EtatSante etat = etatSante(compteur);

                Animal animal = new Animal();
                animal.setCode(String.format("%010d", compteur));
                animal.setPrenom(faker.name().firstName());
                animal.setSexe(alea(Sexe.values()));
                animal.setEtatSante(etat);
                animal.setEspece(this.especes.get(nom));
                animal.setEnclos(etat == EtatSante.EN_QUARANTAINE
                        ? this.enclosQuarantaine.get(compteur % this.enclosQuarantaine.size())
                        : disponibles.get(j));

                this.animaux.add(this.animalService.create(animal));
                compteur++;
            }
        }
    }

    /** Réparti de façon déterministe pour garantir des animaux à soigner. */
    private EtatSante etatSante(int compteur) {
        if (compteur % 11 == 0) {
            return EtatSante.EN_QUARANTAINE;
        }
        if (compteur % 5 == 0) {
            return EtatSante.BLESSE;
        }
        if (compteur % 7 == 0) {
            return EtatSante.MALADE;
        }
        return EtatSante.EN_BONNE_SANTE;
    }

    private void chargePersonnels() {
        NiveauHabilitation[] niveaux = NiveauHabilitation.values();
        for (int i = 0; i < 12; i++) {
            Personnel personnel = new Personnel();
            personnel.setCode(String.format("%010d", 1000000000L + i));
            personnel.setNom(texteAuMoins3(faker.name().lastName()));
            personnel.setPrenom(texteAuMoins3(faker.name().firstName()));
            // 3 personnels par niveau : chaque type d'intervention trouvera des habilités
            personnel.setNiveauHabilitation(niveaux[i % niveaux.length]);

            this.personnels.add(this.personnelService.create(personnel));
        }
    }

    /** nom et prenom exigent 3 caractères minimum. */
    private String texteAuMoins3(String valeur) {
        return valeur.length() >= 3 ? valeur : valeur + faker.letterify("??");
    }

    private void chargeInterventions() {
        int compteur = 0;
        for (TypeIntervention type : TypeIntervention.values()) {
            for (int j = 0; j < 2; j++) {
                EtatIntervention etat = alea(EtatIntervention.values());

                Intervention intervention = new Intervention();
                intervention.setType(type);
                intervention.setEtat(etat);
                intervention.setCode(lettre(type) + String.format("%09d", compteur));
                intervention.setDateDebut(dateDebut(etat));
                intervention.setDateFin(intervention.getDateDebut().plusHours(3));

                cibles(type, compteur).forEach(intervention::addAnimal);
                habilites(type).forEach(intervention::addPersonnel);

                this.interventionService.create(intervention);
                compteur++;
            }
        }
    }

    private LocalDateTime dateDebut(EtatIntervention etat) {
        return switch (etat) {
            case PLANIFIEES -> LocalDateTime.now().plusDays(faker.number().numberBetween(2, 15));
            case EN_COURS -> LocalDateTime.now().minusHours(1);
            case TERMINES, ANNULEE -> LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30));
        };
    }

    /** Un soin médical vise un animal blessé ou malade, pas un sujet en pleine forme. */
    private List<Animal> cibles(TypeIntervention type, int compteur) {
        List<Animal> candidats = this.animaux.stream()
                .filter(animal -> switch (type) {
                    case SOIN_MEDICAL -> animal.getEtatSante() == EtatSante.BLESSE
                            || animal.getEtatSante() == EtatSante.MALADE;
                    case CAPTURE_URGENTE -> animal.getEspece().getDangerosite() == Dangerosite.CRITIQUE;
                    default -> animal.getEtatSante() != EtatSante.DECEDE;
                })
                .toList();

        return candidats.isEmpty()
                ? List.of()
                : List.of(candidats.get(compteur % candidats.size()));
    }

    /** Personnels dont l'habilitation atteint le niveau exigé par le type d'intervention. */
    private List<Personnel> habilites(TypeIntervention type) {
        int requis = type.getNiveauMinimaleRequise().getNiveauHabilitation();
        return this.personnels.stream()
                .filter(personnel -> personnel.getNiveauHabilitation().getNiveauHabilitation() >= requis)
                .limit(2)
                .toList();
    }
}