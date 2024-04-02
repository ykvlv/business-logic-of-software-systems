package ykvlv.blss.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Поваренок ру")
						.version("v1")
						.description("Поваренок ру - это сервис для хранения и поиска рецептов блюд."))
				.components(new Components()
						.addSecuritySchemes("basicAuth", new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("basic")))
				.addSecurityItem(new SecurityRequirement().addList("basicAuth"));
	}

}
