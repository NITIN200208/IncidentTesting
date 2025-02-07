package com.example.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    //call other API(pincode)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
