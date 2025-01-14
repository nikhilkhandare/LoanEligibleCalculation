package com.example.LoanEligibilityCalculation.Service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "loanRestTemplate")
    public RestTemplate restTemplate() {
            return new RestTemplate();
    }
}
