package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Test: pom.xml + Java file change (MAJOR impact)
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Hello World");
		System.out.println("Helloo World");
		SpringApplication.run(DemoApplication.class, args);
	}

}
