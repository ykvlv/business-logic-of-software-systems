package ykvlv.blss.posterservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@EnableRabbit
@SpringBootApplication
@ConfigurationPropertiesScan
public class PosterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosterServiceApplication.class, args);
	}

}
