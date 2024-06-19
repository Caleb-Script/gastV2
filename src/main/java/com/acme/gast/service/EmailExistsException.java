
package com.acme.gast.service;

import lombok.Getter;

/**
 * Exception, falls die Emailadresse bereits existiert.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Getter
public class EmailExistsException extends RuntimeException {
    /**
     * Bereits vorhandene Emailadresse.
     */
    private final String email;

    EmailExistsException(@SuppressWarnings("ParameterHidesMemberVariable") final String email) {
        super("Die Emailadresse " + email + " existiert bereits");
        this.email = email;
    }
}
