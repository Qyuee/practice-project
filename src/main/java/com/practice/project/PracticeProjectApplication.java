package com.practice.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableJpaAuditing
@SpringBootApplication
public class PracticeProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(PracticeProjectApplication.class, args);
	}
}
