package org.dbp.hackaton.hackaton1;

import org.dbp.hackaton.hackaton1.config.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class Hackaton1Application {

	public static void main(String[] args) {
		SpringApplication.run(Hackaton1Application.class, args);
	}

}
