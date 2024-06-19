
package com.acme.gast;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Spring-Konfiguration für Properties "app.keycloak.*".
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 * @param host Rechnername des Keycloak-Servers
 * @param clientSecret Client-Secret gemäß der Client-Konfiguration in Keycloak
 */
@ConfigurationProperties(prefix = "app.keycloak")
public record KeycloakProps(
    @DefaultValue("http")
    String schema,

    @DefaultValue("localhost")
    String host,

    @DefaultValue("8880")
    int port,

    String clientSecret) {
}
