package org.ferialab.server;

import org.openxava.spring.OpenXavaApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends OpenXavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
