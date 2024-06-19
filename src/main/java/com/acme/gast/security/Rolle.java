
package com.acme.gast.security;

import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

/**
 * Singleton für verfügbare Rollen als Strings für das Spring-Interface GrantedAuthority.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@RequiredArgsConstructor
public enum Rolle {
    /**
     * Die Rolle ADMIN.
     */
    ADMIN("ADMIN"),

    /**
     * Die Rolle USER.
     */
    USER("USER");
    private final String value;

    /**
     * Zu einem String die Rolle als Enum ermitteln.
     *
     * @param str String einer Rolle
     * @return Rolle als Enum oder null
     */
    public static Rolle of(final String str) {
        return Stream.of(values())
            .filter(rolle -> rolle.name().equalsIgnoreCase(str))
            .findFirst()
            .orElse(null);
    }
}
