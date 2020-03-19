package com.cwd.tg.ugc.security;

import com.cwd.tg.ugc.errors.exceptions.MissingHeaderException;
import com.cwd.tg.ugc.errors.exceptions.UnauthorizedTokenException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class TokenAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String REQUEST_ID = "request_id";
    private static final String AUTHORIZATION = "Authorization";

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        var token = Optional
                .ofNullable(serverWebExchange.getRequest().getHeaders().get(AUTHORIZATION))
                .map(headers -> headers.get(0))
                .orElseThrow(() -> new UnauthorizedTokenException("Authorization header is missing"));

        var requestId = Optional
                .ofNullable(serverWebExchange.getRequest().getHeaders().get(REQUEST_ID))
                .map(headers -> headers.get(0))
                .orElseThrow(() -> new MissingHeaderException("Request id header is missing"));

        return Mono.just(new Authorization(null, new AuthHeaders(requestId, token)));
    }
}
