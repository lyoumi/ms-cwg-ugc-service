package com.cwd.tg.ugc.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.cwd.tg.ugc.data.input.PlayerCharacterInput;
import com.cwd.tg.ugc.data.internal.character.GameCharacter;
import com.cwd.tg.ugc.handlers.AccountRequestHandler;
import com.cwd.tg.ugc.handlers.GameRequestHandler;
import com.cwd.tg.ugc.security.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterFunctionConfiguration {

    @Bean
    public RouterFunction routerFunction(GameRequestHandler gameRequestHandler,
            AccountRequestHandler accountRequestHandler) {
        return route(GET("/private/rest/game/{id}"), request ->
                ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                        .flatMap(gameRequestHandler::getUserGameCharacter), GameCharacter.class))
                .andRoute(POST("/private/rest/game"), request ->
                        ServerResponse.ok().body(request.bodyToMono(PlayerCharacterInput.class)
                                .flatMap(gameRequestHandler::createGameCharacter), GameCharacter.class))
                .andRoute(GET("/private/auth/user/my"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(accountRequestHandler.getUserDetails())
                                .flatMap(userMono -> userMono), User.class));
    }

}
