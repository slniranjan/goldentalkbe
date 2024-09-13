package com.goldentalk.gt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableAspectJAutoProxy  
@OpenAPIDefinition(
    info = @Info(
            title = "Golden talk  REST API Documentation",
            description = "Golden talk REST API Documentation",
            version = "v1",
            contact = @Contact(
                    name = "",
                    email = "tutor@goldentalk.com",
                    url = ""
            ),
            license = @License(
                    name = "Apache 2.0",
                    url = ""
            )
    ),
    externalDocs = @ExternalDocumentation(
            description =  "Golden talk REST API Documentation",
            url = ""
    )
)
public class GtApplication {

	public static void main(String[] args) {
		SpringApplication.run(GtApplication.class, args);
	}

}
