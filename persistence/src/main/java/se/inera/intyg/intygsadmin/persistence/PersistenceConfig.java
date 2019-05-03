package se.inera.intyg.intygsadmin.persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsadmin.persistence")
@EntityScan(basePackages = "se.inera.intyg.intygsadmin.persistence")
@EnableJpaRepositories(basePackages = "se.inera.intyg.intygsadmin.persistence")
public class PersistenceConfig {

}
