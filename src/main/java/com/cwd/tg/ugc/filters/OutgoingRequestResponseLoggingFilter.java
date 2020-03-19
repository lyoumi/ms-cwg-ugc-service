package com.cwd.tg.ugc.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class OutgoingRequestResponseLoggingFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return  Mono.fromRunnable(() ->
                log.info("Outgoing request {} {} with headers {}",
                        request.method(), request.url(), request.headers()))
                .then(next.exchange(request).doOnSuccess(clientResponse ->
                        log.info("Incoming response {} from {} {} headers {}",
                                clientResponse.statusCode(), request.method(), request.url(),
                                clientResponse.headers().asHttpHeaders())));
    }

    @Override
    public ExchangeFunction apply(ExchangeFunction exchange) {
        return exchange.filter(this::filter);
    }
}
