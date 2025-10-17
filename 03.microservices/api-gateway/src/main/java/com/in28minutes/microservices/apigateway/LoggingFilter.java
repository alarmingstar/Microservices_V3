package com.in28minutes.microservices.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {

    private final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        logger.info("Path of the request received -> {}",
                exchange.getRequest().getPath());
        return chain.filter(exchange);
    }

}

//Aspect	           Explanation
//What it is	       A global request logging filter in Spring Cloud Gateway
//Implements	       GlobalFilter (applies to all routes)
//Reactive type	       Returns Mono<Void> (non-blocking reactive stream)
//Framework	           Built on Spring WebFlux & Project Reactor
//Purpose	           Log the incoming request path without blocking threads
//Approach	           Reactive programming (asynchronous, non-blocking)