package com.cwd.tg.ugc.filters;

import com.cwd.tg.ugc.errors.exceptions.MissingHeaderException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class IncomingRequestResponseLoggingFilter implements WebFilter {

    private static final String REQUEST_ID_HEADER = "request_id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(exchange.getRequest())
                .doOnSuccess(this::logRequest)
                .map(serverHttpRequest -> serverHttpRequest.getHeaders().get(REQUEST_ID_HEADER))
                .switchIfEmpty(Mono.error(new MissingHeaderException("Request id header is missing")))
                .doOnSuccess(header -> exchange.getResponse().getHeaders().add(REQUEST_ID_HEADER, header.toString()))
                .then(chain.filter(exchange))
                .doOnSuccess(aVoid -> logResponse(exchange.getRequest(), exchange.getResponse()))
                .doOnError(throwable -> log.error("Unable to process request {} {} {}",
                        exchange.getRequest().getHeaders().get(REQUEST_ID_HEADER),
                        exchange.getRequest().getMethod(),
                        exchange.getRequest().getURI(),
                        throwable));
    }

    private void logResponse(ServerHttpRequest request, ServerHttpResponse response) {
        log.info("Outgoing response {} from {} {} with headers {}",
                response.getStatusCode(), request.getMethod(), request.getURI(), response.getHeaders());
    }

    private void logRequest(ServerHttpRequest request) {
        log.info("Incoming request {} {} with headers {}",
                request.getMethod(), request.getURI(), request.getHeaders());
    }
}
