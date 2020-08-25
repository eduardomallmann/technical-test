package com.eduardomallmann.compasso.technicaltest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application main class, initiate and run the application.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@EnableAsync
@SpringBootApplication
public class TechnicalTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechnicalTestApplication.class, args);
    }

}
