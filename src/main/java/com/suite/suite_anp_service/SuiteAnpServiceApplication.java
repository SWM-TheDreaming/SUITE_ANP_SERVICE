package com.suite.suite_anp_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoAuditing
@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.suite.suite_anp_service.anp")
@EnableMongoRepositories(basePackages = {
		"com.suite.suite_anp_service.alarm",
		"com.suite.suite_anp_service.payment"
})
public class SuiteAnpServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SuiteAnpServiceApplication.class, args);
	}

}
