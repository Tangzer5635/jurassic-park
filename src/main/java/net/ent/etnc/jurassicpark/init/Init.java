package net.ent.etnc.jurassicpark.init;

import com.github.javafaker.Faker;
import net.ent.etnc.jurassicpark.models.*;
import net.ent.etnc.jurassicpark.models.enumerations.*;
import net.ent.etnc.jurassicpark.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Init implements CommandLineRunner {

    private static final int CAPACITE_ENCLOS = 6;
    private static final int CAPACITE_QUARANTAINE = 2;

    private final Faker faker = new Faker(new java.util.Locale("fr"));

    private final AnimalService animalService;
    private final EnclosService enclosService;
    private final EspeceService especeService;
    private final InterventionService interventionService;
    private final PersonnelService personnelService;

    // Références conservées entre les étapes : évite de deviner les IDs générés
    private final Map<NomEspece, List<Enclos>> enclosParEspece = new EnumMap<>(NomEspece.class);
    private final Map<NomEspece, Espece> especes = new EnumMap<>(NomEspece.class);
    private final Map<Animal, Enclos> destinations = new HashMap<>();
    private final List<Enclos> enclosQuarantaine = new ArrayList<>();
    private final List<Enclos> enclosLibres = new ArrayList<>();
    private final List<Animal> animaux = new ArrayList<>();
    private final List<Animal> animauxCritiques = new ArrayList<>();
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
            case EQUARRISSAGE -> 'E';
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

    private Enclos creerEnclos(TypeEnclos type, SecuriteEnclos securite, int capacite) {
        Enclos enclos = new Enclos();
        enclos.setType(type);
        enclos.setNiveauSecurite(securite);
        // ACTIF obligatoire : un enclos FERME, EVACUE ou en MAINTENANCE ne peut rien accueillir
        enclos.setEtat(EtatEnclos.ACTIF);
        enclos.setCapaciteMax(capacite);
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
            lot.add(creerEnclos(type, securite, CAPACITE_ENCLOS));
            lot.add(creerEnclos(type, securite, CAPACITE_ENCLOS));
            this.enclosParEspece.put(nom, lot);
        }

        // Quarantaine : sécurité maximale, rattachée à aucune espèce
        for (int i = 0; i < 3; i++) {
            this.enclosQuarantaine.add(
                    creerEnclos(TypeEnclos.QUARANTAINE, SecuriteEnclos.MAXIMUM, CAPACITE_QUARANTAINE));
        }

        // Enclos sans occupant : cibles des interventions de nettoyage
        for (int i = 0; i < 3; i++) {
            this.enclosLibres.add(
                    creerEnclos(TypeEnclos.TERRESTRE, SecuriteEnclos.STANDARD, CAPACITE_ENCLOS));
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

            // 2 animaux par espèce, 1 par enclos : la capacité n'est jamais atteinte
            for (int j = 0; j < 2; j++) {
                EtatSante etat = etatSante(compteur);
                boolean isole = etat == EtatSante.EN_QUARANTAINE;

                Animal animal = new Animal();
                animal.setCode(String.format("%010d", compteur));
                animal.setPrenom(faker.name().firstName());
                animal.setSexe(alea(Sexe.values()));
                animal.setEtatSante(etat);
                animal.setEspece(this.especes.get(nom));
                animal.setEnclos(isole
                        ? this.enclosQuarantaine.get(compteur % this.enclosQuarantaine.size())
                        : disponibles.get(j));

                Animal cree = this.animalService.create(animal);
                this.animaux.add(cree);
                // destination d'un futur déplacement : l'autre enclos de l'espèce
                this.destinations.put(cree, disponibles.get(isole ? 0 : 1 - j));
                if (nom.getDangerosite() == Dangerosite.CRITIQUE) {
                    this.animauxCritiques.add(cree);
                }
                compteur++;
            }
        }
    }

    /** Réparti de façon déterministe pour garantir des animaux à soigner. Aucun DECEDE : ils ne se manipulent plus. */
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
                // 1 intervention tous les 3 jours : aucun chevauchement de planning
                LocalDateTime debut = LocalDate.now().minusDays(20).plusDays(compteur * 3L).atTime(9, 0);

                Intervention intervention = new Intervention();
                intervention.setType(type);
                intervention.setCode(lettre(type) + String.format("%09d", compteur));
                intervention.setDateDebut(debut);
                intervention.setDateFin(debut.plusHours(3));
                intervention.setEtat(etatIntervention(debut, debut.plusHours(3)));

                switch (type) {
                    // Le nettoyage vise un enclos vide, sans animal concerné
                    case NETTOYAGE -> intervention.setEnclos(
                            this.enclosLibres.get(compteur % this.enclosLibres.size()));

                    // Le déplacement vise l'autre enclos de l'espèce de l'animal
                    case DEPLACEMENT -> {
                        Animal animal = this.animaux.get(compteur % this.animaux.size());
                        intervention.addAnimal(animal);
                        intervention.setEnclos(this.destinations.get(animal));
                    }

                    // La capture d'urgence ne concerne que les espèces CRITIQUE
                    case CAPTURE_URGENTE -> {
                        Animal animal = this.animauxCritiques.get(compteur % this.animauxCritiques.size());
                        intervention.addAnimal(animal);
                        intervention.setEnclos(this.destinations.get(animal));
                    }

                    // Le soin ne vise qu'un animal blessé ou malade
                    case SOIN_MEDICAL -> intervention.addAnimal(animalASoigner(compteur));

                    default -> intervention.addAnimal(this.animaux.get(compteur % this.animaux.size()));
                }

                habilites(type, compteur).forEach(intervention::addPersonnel);

                this.interventionService.create(intervention);
                compteur++;
            }
        }
    }

    private EtatIntervention etatIntervention(LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime maintenant = LocalDateTime.now();
        if (fin.isBefore(maintenant)) {
            return EtatIntervention.TERMINES;
        }
        if (debut.isAfter(maintenant)) {
            return EtatIntervention.PLANIFIEES;
        }
        return EtatIntervention.EN_COURS;
    }

    private Animal animalASoigner(int compteur) {
        List<Animal> soignables = this.animaux.stream()
                .filter(animal -> animal.getEtatSante() == EtatSante.BLESSE
                        || animal.getEtatSante() == EtatSante.MALADE)
                .toList();
        return soignables.get(compteur % soignables.size());
    }

    /** 2 soigneurs dont l'habilitation atteint le niveau exigé, tournants pour éviter les conflits. */
    private List<Personnel> habilites(TypeIntervention type, int compteur) {
        int requis = type.getNiveauMinimumRequis().getNiveauHabilitationInt();
        List<Personnel> candidats = this.personnels.stream()
                .filter(personnel -> personnel.getNiveauHabilitation().getNiveauHabilitationInt() >= requis)
                .toList();

        int debut = compteur % candidats.size();
        return List.of(candidats.get(debut), candidats.get((debut + 1) % candidats.size()));
    }
}