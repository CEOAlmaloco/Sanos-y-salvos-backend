package com.javadiseno.sanosysalvos.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.javadiseno.sanosysalvos.report.client")
public class MsvcReportApplication {

	public static void main(String[] args) {

		SpringApplication.run(MsvcReportApplication.class, args);

		System.out.println("Msvc Report Application Started");
	}
}
