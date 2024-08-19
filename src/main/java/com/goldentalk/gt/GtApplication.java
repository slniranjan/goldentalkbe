package com.goldentalk.gt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class GtApplication {

	public static void main(String[] args) {
		SpringApplication.run(GtApplication.class, args);
	}

}
