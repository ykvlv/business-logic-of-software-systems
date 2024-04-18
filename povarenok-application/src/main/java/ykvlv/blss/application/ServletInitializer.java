package ykvlv.blss.application;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	// Чтобы сервлет-контейнер мог запустить приложение, нужно переопределить метод configure
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PovarenokApplication.class);
	}

}
