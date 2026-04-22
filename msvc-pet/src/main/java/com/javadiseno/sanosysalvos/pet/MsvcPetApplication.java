package com.javadiseno.sanosysalvos.pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.javadiseno.sanosysalvos.pet.client")
public class MsvcPetApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcPetApplication.class, args);
	}

}
