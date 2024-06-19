
package com.acme.gast.dev;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;
import static org.springframework.context.annotation.Bean.Bootstrap.BACKGROUND;

/**
 * Protokoll-Ausgabe, wenn Kubernetes erkannt wird.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
interface K8s {
    /**
     * Protokoll-Ausgabe, wenn Kubernetes erkannt wird.
     *
     * @return Listener für den ApplicationReadyEvent, um Kubernetes zu erkennen.
     */
    @Bean(bootstrap = BACKGROUND)
    @ConditionalOnCloudPlatform(KUBERNETES)
    default ApplicationListener<ApplicationReadyEvent> detectK8s() {
        return _ -> LoggerFactory.getLogger(K8s.class).debug("Plattform \"Kubernetes\"");
    }
}
