package net.ent.etnc.jurassicpark.models.enumerations;

public enum NomEspece {

    // Terrestres
    TYRANNOSAURUS_REX("Tyrannosaurus rex", TypeEspece.TERRESTRE, Alimentation.CARNIVORE, Dangerosite.CRITIQUE),
    VELOCIRAPTOR("Velociraptor", TypeEspece.TERRESTRE, Alimentation.CARNIVORE, Dangerosite.CRITIQUE),
    CARNOTAURUS("Carnotaurus", TypeEspece.TERRESTRE, Alimentation.CARNIVORE, Dangerosite.ELEVE),
    COTYLORHYNCHUS("Cotylorhynchus", TypeEspece.TERRESTRE, Alimentation.HERBIVORE, Dangerosite.FAIBLE),
    DILOPHOSAURUS("Dilophosaurus", TypeEspece.TERRESTRE, Alimentation.CARNIVORE, Dangerosite.ELEVE),
    COMPSOGNATHUS("Compsognathus", TypeEspece.TERRESTRE, Alimentation.CARNIVORE, Dangerosite.FAIBLE),
    GALLIMIMUS("Gallimimus", TypeEspece.TERRESTRE, Alimentation.OMNIVORE, Dangerosite.FAIBLE),
    TRICERATOPS("Triceratops", TypeEspece.TERRESTRE, Alimentation.HERBIVORE, Dangerosite.MODERE),
    STEGOSAURUS("Stegosaurus", TypeEspece.TERRESTRE, Alimentation.HERBIVORE, Dangerosite.MODERE),
    ANKYLOSAURUS("Ankylosaurus", TypeEspece.TERRESTRE, Alimentation.HERBIVORE, Dangerosite.MODERE),
    BRACHIOSAURUS("Brachiosaurus", TypeEspece.TERRESTRE, Alimentation.HERBIVORE, Dangerosite.FAIBLE),
    PARASAUROLOPHUS("Parasaurolophus", TypeEspece.TERRESTRE, Alimentation.HERBIVORE, Dangerosite.FAIBLE),

    // Aquatiques
    MOSASAURUS("Mosasaurus", TypeEspece.AQUATIQUE, Alimentation.CARNIVORE, Dangerosite.CRITIQUE),
    PLESIOSAURUS("Plesiosaurus", TypeEspece.AQUATIQUE, Alimentation.CARNIVORE, Dangerosite.ELEVE),
    ICHTYOSAURUS("Ichthyosaurus", TypeEspece.AQUATIQUE, Alimentation.CARNIVORE, Dangerosite.MODERE),

    // Volants
    QUETZALCOATLUS("Quetzalcoatlus", TypeEspece.VOLANT, Alimentation.CARNIVORE, Dangerosite.ELEVE),
    PTERANODON("Pteranodon", TypeEspece.VOLANT, Alimentation.CARNIVORE, Dangerosite.ELEVE),
    DIMORPHODON("Dimorphodon", TypeEspece.VOLANT, Alimentation.CARNIVORE, Dangerosite.MODERE);

    private final String libelle;
    private final TypeEspece type;
    private final Alimentation alimentation;
    private final Dangerosite dangerosite;

    private NomEspece(String libelle, TypeEspece type, Alimentation alimentation, Dangerosite dangerosite) {
        this.libelle = libelle;
        this.type = type;
        this.alimentation = alimentation;
        this.dangerosite = dangerosite;
    }

    public String getLibelle() { return libelle; }

    public TypeEspece getType() { return type; }

    public Alimentation getAlimentation() { return alimentation; }

    public Dangerosite getDangerosite() { return dangerosite; }
}