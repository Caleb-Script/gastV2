package com.acme.gast.controller;

import com.acme.gast.entity.Gast;
import jakarta.validation.ConstraintViolation;
import java.util.Collection;
import lombok.Getter;

/**
 * Exception, falls es mindestens ein verletztes Constraint gibt.
 *
 * @author <a href="mailto:Caleb_G@outlook.de">Caleb Gyamfi</a>
 */
@Getter
public class ConstraintViolationsException extends RuntimeException {
    /**
     * Die verletzten Constraints.
     */
    private final transient Collection<ConstraintViolation<Gast>> violations;

    ConstraintViolationsException(
        @SuppressWarnings("ParameterHidesMemberVariable")
        final Collection<ConstraintViolation<Gast>> violations
    ) {
        super("Constraints sind verletzt");
        this.violations = violations;
    }
}
