package com.in28minutes.microservices.currencyconversionservice;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}


// If you do not need to customize the RestTemplate, it is not strictly necessary to configure it.
// Spring Boot autoconfigures a default RestTemplateBuilder, and you can create a RestTemplate instance
// directly in your service or inject the builder if needed.
//
// However, defining a RestTemplate bean is useful if you want to reuse it across your application
// or apply customizations (e.g., timeouts, interceptors). If you only use default settings
// and do not inject RestTemplate anywhere, you can omit this configuration.