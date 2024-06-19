
package com.acme.gast.service;

import com.acme.gast.security.Rolle;
import lombok.Getter;

import java.util.Collection;

/**
 * Exception, falls der Zugriff wegen fehlender Rollen nicht erlaubt ist.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Getter
public class AccessForbiddenException extends RuntimeException {
    /**
     * Vorhandene Rollen.
     */
    private final Collection<Rolle> rollen;

    @SuppressWarnings("ParameterHidesMemberVariable")
    AccessForbiddenException(final Collection<Rolle> rollen) {
        super("Unzureichende Rollen: " + rollen);
        this.rollen = rollen;
    }
}
