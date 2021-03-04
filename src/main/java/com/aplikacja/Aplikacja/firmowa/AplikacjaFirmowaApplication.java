package com.aplikacja.Aplikacja.firmowa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@RestController

public class AplikacjaFirmowaApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AplikacjaFirmowaApplication.class, args);
	}

}
