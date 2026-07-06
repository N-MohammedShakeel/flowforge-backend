package com.ms.flowforge;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableAsync
@OpenAPIDefinition(
		info = @Info(
				title = "FlowForge REST API Documentation",
				description = "FlowForge Backend - AI-powered architecture generation platform REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "FlowForge Team",
						email = "support@flowforge.com",
						url = "https://www.flowforge.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.flowforge.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "FlowForge REST API Documentation",
				url = "https://www.flowforge.com/swagger-ui.html"
		),
		security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
public class FlowforgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowforgeApplication.class, args);
	}

}
