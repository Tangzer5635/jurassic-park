package net.ent.etnc.jurassicpark.business.validators;

import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Intervention;
import org.springframework.stereotype.Component;

@Component
public class InterventionValidator {

    private final CaptureValidator captureValidator;
    private final DeplacementValidator deplacementValidator;
    private final EquarrissageValidator equarrissageValidator;
    private final NettoyageValidator nettoyageValidator;
    private final NourrissageSoinValidator nourrissageSoinValidator;
    private final SurveillanceValidator surveillanceValidator;

    public InterventionValidator(
            CaptureValidator captureValidator,
            DeplacementValidator deplacementValidator,
            EquarrissageValidator equarrissageValidator,
            NettoyageValidator nettoyageValidator,
            NourrissageSoinValidator nourrissageSoinValidator,
            SurveillanceValidator surveillanceValidator) {

        this.captureValidator = captureValidator;
        this.deplacementValidator = deplacementValidator;
        this.equarrissageValidator = equarrissageValidator;
        this.nettoyageValidator = nettoyageValidator;
        this.nourrissageSoinValidator = nourrissageSoinValidator;
        this.surveillanceValidator = surveillanceValidator;
    }

    public ResultatValidation valider(Intervention intervention) {

        return switch (intervention.getType()) {

            case CAPTURE_URGENTE -> captureValidator.valider(intervention);
            case DEPLACEMENT -> deplacementValidator.valider(intervention);
            case EQUARRISSAGE -> equarrissageValidator.valider(intervention);
            case NETTOYAGE -> nettoyageValidator.valider(intervention);
            case NOURRISSAGE, SOIN_MEDICAL -> nourrissageSoinValidator.valider(intervention);
            case SURVEILLANCE -> surveillanceValidator.valider(intervention);
        };
    }
}
