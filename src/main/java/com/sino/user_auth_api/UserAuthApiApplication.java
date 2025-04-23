package com.sino.user_auth_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserAuthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthApiApplication.class, args);
	}

}
