package org.samlikescode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.samlikescode.databind.CustomObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@ComponentScan
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        return new CustomObjectMapper();
    }
}
