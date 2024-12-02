package net.coursemaker.backendv2.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configurable
public class SwaggerConfig {

	@Bean
	public OpenAPI api() {
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
			.info(apiInfo());
	}

	private Info apiInfo() {
		Contact contact = new Contact();
		contact.setEmail("gurwls0214@naver.com");
		contact.setName("coursemaker");
		contact.setUrl("http://course-maker.net");


		return new Info()
			.title("Course Maker API")
			.description("코스메이커 API 스웨거 입니다.")
			.version("0.1.0")
			.contact(contact);
	}
}
