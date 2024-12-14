package net.coursemaker.backendv2.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@Profile({"local", "dev"})
public class SwaggerConfigDev {

	@Bean
	public OpenAPI api() {
		Server server = new Server();
		server.setUrl("https://api.dev.course-maker.net:9191");
		server.setDescription("코스메이커 요청 서버");

		SecurityScheme apiKey = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.in(SecurityScheme.In.HEADER)
			.name("Authorization")
			.scheme("bearer")
			.bearerFormat("JWT");

		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("Bearer Token");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("Bearer Token", apiKey))
			.addSecurityItem(securityRequirement)
			.servers(List.of(server))
			.info(apiInfo());
	}

	private Info apiInfo() {
		Contact contact = new Contact();
		contact.setEmail("gurwls0214@naver.com");
		contact.setName("coursemaker");
		contact.setUrl("http://course-maker.net");


		return new Info()
			.title("[DEV] Course Maker Develop API")
			.description("코스메이커 개발서버 API 스웨거 입니다.")
			.version("0.1.0")
			.contact(contact);
	}
}
