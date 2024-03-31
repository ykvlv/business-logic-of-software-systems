package ykvlv.blss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BLSSApplication {

	public static void main(String[] args) {
		SpringApplication.run(BLSSApplication.class, args);
	}

}
