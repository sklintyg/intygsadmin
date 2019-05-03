package se.inera.intyg.intygsadmin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import se.inera.intyg.intygsadmin.persistence.PersistenceConfig;

@SpringBootApplication
@Import({
		PersistenceConfig.class
})
public class IntygsadminApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntygsadminApplication.class, args);
	}

}
