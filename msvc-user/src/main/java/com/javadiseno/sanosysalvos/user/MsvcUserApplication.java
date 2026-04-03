package com.javadiseno.sanosysalvos.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication()
public class MsvcUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcUserApplication.class, args);
	}

}
