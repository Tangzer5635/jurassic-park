package net.ent.etnc.jurassicpark.business.rules;

import net.ent.etnc.jurassicpark.business.commons.ErreurValidation;
import net.ent.etnc.jurassicpark.business.commons.ResultatValidation;
import net.ent.etnc.jurassicpark.models.Intervention;
import net.ent.etnc.jurassicpark.models.Personnel;
import net.ent.etnc.jurassicpark.services.PersonnelService;
import org.springframework.stereotype.Component;

@Component
public class PersonnelDisponible {

    private final PersonnelService personnelService;

    public PersonnelDisponible(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    public ResultatValidation verifier(
            Intervention intervention
    ) {

        for (Personnel p : intervention.getPersonnels()) {
            if (!personnelService.personnelEstDisponible(p.getId(), intervention.getDateDebut(), intervention.getDateFin())) {
                return ResultatValidation.invalide(ErreurValidation.PERSONNEL_NON_DISPONIBLE);
            }
        }

        return ResultatValidation.valide();
    }
}