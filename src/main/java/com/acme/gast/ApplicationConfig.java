
package com.acme.gast;

import com.acme.gast.security.KeycloakClientConfig;
import com.acme.gast.security.SecurityConfig;

/**
 * Konfigurationsklasse für die Anwendung bzw. den Microservice.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
final class ApplicationConfig implements SecurityConfig, KeycloakClientConfig {
    ApplicationConfig() {
    }
}
