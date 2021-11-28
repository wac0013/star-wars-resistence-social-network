package br.com.swrsn;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
		title = "API LMS - EAD-Rocks",
		description = "Endpoints API BFF para projeto front-end"),
		servers = @Server(url = "http://localhost:8080"))
@SpringBootApplication
public class SwrsnApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwrsnApplication.class, args);
	}

}
