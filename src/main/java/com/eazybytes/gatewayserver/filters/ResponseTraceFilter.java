package com.eazybytes.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilter {

    @Autowired
    private FilterUtility filterUtility;
    private final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Bean
    public GlobalFilter postGlobalFilter() {
        return ((exchange, chain) ->
                chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                    String correlationId = filterUtility.getCorrelationId(requestHeaders);
                    if (correlationId != null) {
                        logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
                        exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
                    } else {
                        logger.debug("There is no CORRELATION_ID avilable into request header..");
                    }
                }))
        );
    }
}
