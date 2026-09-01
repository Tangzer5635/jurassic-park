package net.ent.etnc.jurassicpark.controllers.commons;

import jakarta.validation.ConstraintViolationException;
import net.ent.etnc.jurassicpark.services.commons.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GestionnaireExceptions {

    /** Règle métier violée : la requête est bien formée mais refusée. */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> regleMetier(ServiceException ex) {
        return corps(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Contraintes Bean Validation sur l'entité. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> validation(ConstraintViolationException ex) {
        String details = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " : " + violation.getMessage())
                .collect(Collectors.joining(" ; "));
        return corps(HttpStatus.BAD_REQUEST, details);
    }

    /** Contraintes sur un @RequestBody annoté @Valid. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validationCorps(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(erreur -> erreur.getField() + " : " + erreur.getDefaultMessage())
                .collect(Collectors.joining(" ; "));
        return corps(HttpStatus.BAD_REQUEST, details);
    }

    /** JSON illisible ou enum inconnu. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalide(HttpMessageNotReadableException ex) {
        return corps(HttpStatus.BAD_REQUEST, "Corps de requête illisible");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> technique(Exception ex) {
        ex.printStackTrace();
        return corps(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> ressourceInexistante(NoResourceFoundException ex) {
        return corps(HttpStatus.NOT_FOUND, "Ressource inexistante");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> parametreInvalide(MethodArgumentTypeMismatchException ex) {
        return corps(HttpStatus.BAD_REQUEST, "Valeur invalide pour le paramètre " + ex.getName());
    }

    private ResponseEntity<Map<String, Object>> corps(HttpStatus statut, String message) {
        return ResponseEntity.status(statut).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", statut.value(),
                "erreur", statut.getReasonPhrase(),
                "message", message == null ? "" : message));
    }
}