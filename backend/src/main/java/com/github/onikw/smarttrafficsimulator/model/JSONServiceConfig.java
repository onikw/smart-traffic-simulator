package com.github.onikw.smarttrafficsimulator.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
public class JSONServiceConfig {


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
