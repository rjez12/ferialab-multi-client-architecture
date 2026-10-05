package org.ferialab.server.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration {
    @Bean
    WebMvcConfigurer feriaLabCors(@Value("${ferialab.web-origin:http://127.0.0.1:4173}") String webOrigin) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1/**")
                        .allowedOrigins(webOrigin)
                        .allowedMethods("GET", "OPTIONS")
                        .allowedHeaders("Accept", "Content-Type")
                        .maxAge(600);
            }
        };
    }
}
