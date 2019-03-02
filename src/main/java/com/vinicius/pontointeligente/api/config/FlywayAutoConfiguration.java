package com.vinicius.pontointeligente.api.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayAutoConfiguration {

	Flyway flyway = Flyway.configure()
			.dataSource("jdbc:mysql://localhost:3306/ponto_inteligente", "root", "558525")
			.load();
}