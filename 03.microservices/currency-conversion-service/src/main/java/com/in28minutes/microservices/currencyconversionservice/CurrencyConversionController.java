package com.in28minutes.microservices.currencyconversionservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

@RestController
public class CurrencyConversionController {

    private final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    private final RestTemplate restTemplate;
    private final CurrencyExchangeProxy proxy;

    public CurrencyConversionController(RestTemplate restTemplate, CurrencyExchangeProxy proxy) {
        this.restTemplate = restTemplate;
        this.proxy = proxy;
    }

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {
        logger.debug("Calculating Currency Conversion using Rest Template");

        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

//        ResponseEntity<CurrencyConversion> responseEntity = restTemplate.getForEntity
//                ("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
//                        CurrencyConversion.class, uriVariables);

        ResponseEntity<CurrencyConversion> responseEntity = restTemplate.exchange(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                HttpMethod.GET,
                null,
                CurrencyConversion.class,
                uriVariables
        );

        CurrencyConversion currencyConversion = responseEntity.getBody();

        // Consider generating a builder for CurrencyConversion and building the object using that
        // Use copilot to understand how to do that
        return new CurrencyConversion(currencyConversion.getId(),
                from, to, quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment() + " " + "rest template");

    }

    /**
     * Calculates currency conversion using the Feign client.
     *
     * @param from     the source currency code
     * @param to       the target currency code
     * @param quantity the amount to convert
     * @return a {@link CurrencyConversion} object containing conversion details
     */
    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        logger.debug("Calculating Currency Conversion using Feign Client");

        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);

        return new CurrencyConversion(currencyConversion.getId(),
                from, to, quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment() + " " + "feign");

    }


}
