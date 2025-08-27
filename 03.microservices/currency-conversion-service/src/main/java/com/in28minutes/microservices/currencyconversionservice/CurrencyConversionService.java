package com.in28minutes.microservices.currencyconversionservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class CurrencyConversionService {

    private final Logger LOGGER = LoggerFactory.getLogger(CurrencyConversionService.class);

    private final RestTemplate restTemplate;
    private final CurrencyExchangeProxy proxy;

    public CurrencyConversionService(RestTemplate restTemplate, CurrencyExchangeProxy proxy) {
        this.restTemplate = restTemplate;
        this.proxy = proxy;
    }

    /**
     * Converts currency from one type to another using a REST template to call an external service.
     *
     * @param from     the source currency code
     * @param to       the target currency code
     * @param quantity the amount to convert
     * @return a {@link CurrencyConversion} object containing conversion details
     */
    public CurrencyConversion convertCurrency(String from, String to, BigDecimal quantity) {
        LOGGER.info("convertCurrency using Rest Template from {} to {}", from, to);

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
     * Converts currency from one type to another using a Feign client to call an external service.
     *
     * @param from     the source currency code
     * @param to       the target currency code
     * @param quantity the amount to convert
     * @return a {@link CurrencyConversion} object containing conversion details
     */
    public CurrencyConversion convertCurrencyFeign(String from, String to, BigDecimal quantity) {
        LOGGER.info("convertCurrency using Feign Client from {} to {}", from, to);
        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);

        return new CurrencyConversion(
                currencyConversion.getId(),
                from, to, quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(currencyConversion.getConversionMultiple()),
                currencyConversion.getEnvironment() + " feign"
        );
    }
}
