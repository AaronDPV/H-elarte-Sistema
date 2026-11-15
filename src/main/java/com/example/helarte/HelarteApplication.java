package com.example.helarte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HelarteApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelarteApplication.class, args);
	}

}
