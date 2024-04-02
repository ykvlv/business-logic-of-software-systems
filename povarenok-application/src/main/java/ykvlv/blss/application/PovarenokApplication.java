package ykvlv.blss.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan
@EntityScan("ykvlv.blss.domain.entity")
@EnableJpaRepositories("ykvlv.blss.domain.repository")
@SpringBootApplication(scanBasePackages = {"ykvlv.blss"})
public class PovarenokApplication {

	public static void main(String[] args) {
		SpringApplication.run(PovarenokApplication.class, args);
	}

}
