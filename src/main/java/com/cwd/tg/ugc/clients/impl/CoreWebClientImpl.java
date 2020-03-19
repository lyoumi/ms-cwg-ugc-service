package com.cwd.tg.ugc.clients.impl;

import static com.cwd.tg.ugc.security.SecurityUtils.getAuthHeaders;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.cwd.tg.ugc.clients.CoreWebClient;
import com.cwd.tg.ugc.data.internal.character.GameCharacter;
import com.cwd.tg.ugc.errors.exceptions.GameCharacterNotFoundException;
import com.cwd.tg.ugc.errors.exceptions.InternalCommunicationException;
import com.cwd.tg.ugc.errors.exceptions.ServiceNotAvailableException;
import com.cwd.tg.ugc.errors.http.HttpErrorMessage;
import com.cwd.tg.ugc.security.AuthHeaders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

//TODO: FTCW-8 - Replace with feign clients
@Slf4j
@Component
@RequiredArgsConstructor
public class CoreWebClientImpl implements CoreWebClient {

    private static final String REQUEST_ID = "request_id";
    private static final String AUTHORIZATION = "Authorization";

    private static final String GET_CHARACTER_FORMAT = "%s/private/character/user/%s/";
    private static final String SAVE_CHARACTER_FORMAT = "%s/private/character/";

    @Value("${services.core.instance.name}")
    private String coreInstanceName;

    private final LoadBalancerClient loadBalancerClient;
    private final WebClient webClient;

    @Override
    public Mono<GameCharacter> getUserCharacter(String userId) {
        var coreBaseUrl = getCoreBaseUrl();

        return getAuthHeaders()
                .flatMap(authHeaders ->
                        webClient
                                .get()
                                .uri(format(GET_CHARACTER_FORMAT, coreBaseUrl, userId))
                                .headers(getHttpHeaders(authHeaders))
                                .retrieve()
                                .onStatus(httpStatus -> httpStatus.equals(NOT_FOUND),
                                        clientResponse -> Mono.error(
                                                new GameCharacterNotFoundException(
                                                        format("Unable to get character by user id %s", userId))))
                                .onStatus(HttpStatus::isError, this::mapErrorResponses)
                                .bodyToMono(GameCharacter.class));
    }

    @Override
    public Mono<GameCharacter> saveUserCharacter(GameCharacter gameCharacter) {
        var coreBaseUrl = getCoreBaseUrl();

        return getAuthHeaders()
                .flatMap(authHeaders ->
                        webClient
                                .post()
                                .uri(format(SAVE_CHARACTER_FORMAT, coreBaseUrl))
                                .headers(getHttpHeaders(authHeaders))
                                .body(BodyInserters.fromObject(gameCharacter))
                                .retrieve()
                                .onStatus(HttpStatus::isError, this::mapErrorResponses)
                                .bodyToMono(GameCharacter.class));
    }

    private Consumer<HttpHeaders> getHttpHeaders(AuthHeaders authHeaders) {
        return httpHeaders ->
                httpHeaders.putAll(
                        Map.of(
                                REQUEST_ID, singletonList(authHeaders.getRequestId()),
                                AUTHORIZATION, singletonList(authHeaders.getAuthToken())));
    }

    private String getCoreBaseUrl() {
        return Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                format("Service {%s} currently is not available.", coreInstanceName)));
    }

    private Mono<? extends Throwable> mapErrorResponses(ClientResponse clientResponse) {
        return Mono.error(new InternalCommunicationException(format("Internal communication failure: status %s body %s",
                clientResponse.statusCode(), clientResponse.bodyToMono(HttpErrorMessage.class))));
    }
}
