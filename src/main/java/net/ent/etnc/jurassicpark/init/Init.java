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
    private final List<Animal> animauxSains = new ArrayList<>();
    private final List<Animal> animauxSoignables = new ArrayList<>();
    private final List<Animal> animauxMorts = new ArrayList<>();
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

    private char lettre(TypeEnclos type) {
        return switch (type) {
            case AQUATIQUE -> 'A';
            case TERRESTRE -> 'T';
            case VOLIERE -> 'V';
            case QUARANTAINE -> 'Q';
            case CIMETIERE -> 'C';
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
        // ACTIF obligatoire : EnclosDestinationCompatible rejette tout autre état
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

        // Quarantaine : accueille les animaux blessés et malades
        for (int i = 0; i < 3; i++) {
            this.enclosQuarantaine.add(
                    creerEnclos(TypeEnclos.QUARANTAINE, SecuriteEnclos.MAXIMUM, CAPACITE_QUARANTAINE));
        }

        // Enclos sans occupant ni déplacement prévu : seuls candidats au nettoyage
        for (int i = 0; i < 3; i++) {
            this.enclosLibres.add(
                    creerEnclos(TypeEnclos.TERRESTRE, SecuriteEnclos.STANDARD, CAPACITE_ENCLOS));
        }

        // Cimetière : destination des cadavres après équarrissage
        creerEnclos(TypeEnclos.CIMETIERE, SecuriteEnclos.STANDARD, 50);
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

            this.especes.put(nom, this.especeService.create(espece));
        }
    }

    private void chargeAnimaux() {
        int compteur = 0;
        for (NomEspece nom : NomEspece.values()) {
            List<Enclos> disponibles = this.enclosParEspece.get(nom);

            // 2 animaux par espèce, 1 par enclos : la capacité reste large
            for (int j = 0; j < 2; j++) {
                EtatSante etat = etatSante(compteur);
                boolean isole = etat == EtatSante.BLESSE || etat == EtatSante.MALADE;

                Animal animal = new Animal();
                animal.setCode(String.format("%010d", compteur));
                animal.setPrenom(faker.name().firstName());
                animal.setSexe(compteur % 2 == 0 ? Sexe.MALE : Sexe.FEMELLE);
                animal.setEtatSante(etat);
                animal.setEspece(this.especes.get(nom));
                // blessés et malades en quarantaine, conformément à EnclosDestinationCompatible
                animal.setEnclos(isole
                        ? this.enclosQuarantaine.get(this.animauxSoignables.size() / CAPACITE_QUARANTAINE)
                        : disponibles.get(j));

                Animal cree = this.animalService.create(animal);
                trier(cree, nom, etat);

                if (!isole) {
                    // destination d'un futur déplacement : l'autre enclos de la même espèce
                    this.destinations.put(cree, disponibles.get(1 - j));
                }
                compteur++;
            }
        }
    }

    private void trier(Animal animal, NomEspece nom, EtatSante etat) {
        switch (etat) {
            case DECEDE -> this.animauxMorts.add(animal);
            case BLESSE, MALADE -> this.animauxSoignables.add(animal);
            default -> {
                this.animauxSains.add(animal);
                if (nom.getDangerosite() == Dangerosite.CRITIQUE) {
                    this.animauxCritiques.add(animal);
                }
            }
        }
    }

    /** Répartition déterministe : 2 morts pour l'équarrissage, 4 soignables, le reste en bonne santé. */
    private EtatSante etatSante(int compteur) {
        return switch (compteur) {
            case 4, 20 -> EtatSante.DECEDE;
            case 8, 24 -> EtatSante.BLESSE;
            case 12, 28 -> EtatSante.MALADE;
            default -> EtatSante.EN_BONNE_SANTE;
        };
    }

    private void chargePersonnels() {
        NiveauHabilitation[] niveaux = NiveauHabilitation.values();
        for (int i = 0; i < 12; i++) {
            Personnel personnel = new Personnel();
            personnel.setCode(String.format("%010d", 1000000000L + i));
            personnel.setNom(texteAuMoins3(faker.name().lastName()));
            personnel.setPrenom(texteAuMoins3(faker.name().firstName()));
            // 3 personnels par niveau : chaque type d'intervention trouve des habilités
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

                remplir(intervention, type, compteur);
                habilites(type, compteur).forEach(intervention::addPersonnel);

                this.interventionService.create(intervention);
                compteur++;
            }
        }
    }

    private void remplir(Intervention intervention, TypeIntervention type, int compteur) {
        switch (type) {
            // Nettoyage : enclos vide, aucun animal concerné
            case NETTOYAGE -> intervention.setEnclos(
                    this.enclosLibres.get(compteur % this.enclosLibres.size()));

            // Déplacement : animal sain vers l'autre enclos de son espèce
            case DEPLACEMENT -> {
                Animal animal = this.animauxSains.get(compteur % this.animauxSains.size());
                intervention.addAnimal(animal);
                intervention.setEnclos(this.destinations.get(animal));
            }

            // Équarrissage : uniquement des animaux décédés
            case EQUARRISSAGE -> intervention.addAnimal(
                    this.animauxMorts.get(compteur % this.animauxMorts.size()));

            // Soin : uniquement des animaux blessés ou malades
            case SOIN_MEDICAL -> intervention.addAnimal(
                    this.animauxSoignables.get(compteur % this.animauxSoignables.size()));

            // Capture d'urgence : espèces critiques, vivantes
            case CAPTURE_URGENTE -> intervention.addAnimal(
                    this.animauxCritiques.get(compteur % this.animauxCritiques.size()));

            // Nourrissage : n'importe quel animal vivant
            case NOURRISSAGE -> intervention.addAnimal(
                    this.animauxSains.get(compteur % this.animauxSains.size()));

            // Surveillance : aucun animal ni enclos requis
            case SURVEILLANCE -> { }
        }
    }

    private EtatIntervention etatIntervention(LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime maintenant = LocalDateTime.now();
        if (fin.isBefore(maintenant)) {
            return EtatIntervention.TERMINEE;
        }
        if (debut.isAfter(maintenant)) {
            return EtatIntervention.PLANIFIEE;
        }
        return EtatIntervention.EN_COURS;
    }

    /** 2 soigneurs au niveau requis, tournants pour éviter les conflits de planning. */
    private List<Personnel> habilites(TypeIntervention type, int compteur) {
        int requis = type.getNiveauMinimumRequis().getNiveauHabilitationInt();
        int nombre = type.getNombreMinimumPersonnel();
        List<Personnel> candidats = this.personnels.stream()
                .filter(personnel -> personnel.getNiveauHabilitation().getNiveauHabilitationInt() >= requis)
                .toList();

        List<Personnel> resultat = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            resultat.add(candidats.get((compteur + i) % candidats.size()));
        }
        return resultat;
    }
}