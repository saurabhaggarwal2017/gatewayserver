package com.eazybytes.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;

@Component
public class FilterUtility {
    public static final String CORRELATION_ID = "eazybank-correlation-id";
    private Logger logger = LoggerFactory.getLogger(FilterUtility.class);

    public String getCorrelationId(HttpHeaders requestHeaders) {
        if (requestHeaders.get(CORRELATION_ID) != null) {
            List<String> requestHeaderList = requestHeaders.get(CORRELATION_ID);
            logger.debug("List :: {}", requestHeaderList);
            return requestHeaderList.stream().findFirst().get();
        }
        return null;
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationID) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(CORRELATION_ID, correlationID).build()).build();
    }

}
