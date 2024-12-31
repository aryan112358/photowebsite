// Main Spring Boot Application Entry Point
package com.photowebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicroserviceApplication {

    public static void main(String[] args) {
        // Starting the Spring Boot application
        SpringApplication.run(MicroserviceApplication.class, args);
    }
}
