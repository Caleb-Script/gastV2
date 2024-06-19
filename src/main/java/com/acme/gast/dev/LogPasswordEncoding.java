package com.acme.gast.dev;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Beim ApplicationReadyEvent wird ein verschlüsseltes Passwort ausgegeben.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
interface LogPasswordEncoding {
    /**
     * Bean-Definition, um einen Listener bereitzustellen, der verschiedene Verschlüsselungsverfahren ausgibt.
     *
     * @param passwordEncoder PasswordEncoder für Argon2
     * @param password Das zu verschlüsselnde Passwort
     * @return Listener für die Ausgabe der verschiedenen Verschlüsselungsverfahren
     */
    @Bean
    default ApplicationListener<ApplicationReadyEvent> logPasswordEncoding(
        final PasswordEncoder passwordEncoder,
        @Value("${app.password}") final String password
    ) {
        return _ -> LoggerFactory
            .getLogger(LogPasswordEncoding.class)
            .atDebug()
            .setMessage("Argon2id mit \"{}\":   {}")
            .addArgument(password)
            .addArgument(() -> passwordEncoder.encode(password))
            .log();
    }
}
