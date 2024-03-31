package ykvlv.blss.povarenok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PovarenokApplication {

	public static void main(String[] args) {
		SpringApplication.run(PovarenokApplication.class, args);
	}

}
