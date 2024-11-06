package com.teammanagement.footballteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;




@SpringBootApplication
@EnableWebMvc
public class FootballteamApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballteamApplication.class, args);
	}

}
