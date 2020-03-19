package com.cwd.tg.ugc.configuration;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import com.cwd.tg.ugc.errors.exceptions.GameCharacterNotFoundException;
import com.cwd.tg.ugc.errors.exceptions.MissingHeaderException;
import com.cwd.tg.ugc.errors.exceptions.ServiceNotAvailableException;
import com.cwd.tg.ugc.errors.exceptions.TokenValidationException;
import com.cwd.tg.ugc.errors.exceptions.UnauthorizedTokenException;
import com.cwd.tg.ugc.errors.handlers.CommonExceptionHandler;
import com.cwd.tg.ugc.errors.http.HttpErrorMessage;
import com.cwd.tg.ugc.errors.exceptions.SecurityContextEmptyException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ExceptionHandlingConfiguration {

    private static final Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> EXCEPTION_MAPPINGS =
            Map.of(
                    MissingHeaderException.class, exception ->
                            ServerResponse.status(BAD_REQUEST)
                                    .body(fromObject(new HttpErrorMessage(1024000,
                                            BAD_REQUEST.getReasonPhrase(),
                                            exception.getMessage()))),

                    UnauthorizedTokenException.class, exception -> ServerResponse.status(UNAUTHORIZED).body(fromObject(
                            new HttpErrorMessage(1024010, UNAUTHORIZED.getReasonPhrase(),
                                    UNAUTHORIZED.getReasonPhrase()))),

                    TokenValidationException.class, exception -> ServerResponse.status(FORBIDDEN)
                            .body(fromObject(new HttpErrorMessage(1024030, FORBIDDEN.getReasonPhrase(),
                                    FORBIDDEN.getReasonPhrase()))),

                    GameCharacterNotFoundException.class, exception ->
                            ServerResponse.status(NOT_FOUND)
                                    .body(fromObject(new HttpErrorMessage(1024040,
                                            NOT_FOUND.getReasonPhrase(),
                                            exception.getMessage()))),


                    SecurityContextEmptyException.class, exception ->
                            ServerResponse.status(INTERNAL_SERVER_ERROR)
                                    .body(fromObject(new HttpErrorMessage(1025000,
                                            INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                            INTERNAL_SERVER_ERROR.getReasonPhrase()))),

                    ServiceNotAvailableException.class, exception ->
                            ServerResponse.status(SERVICE_UNAVAILABLE)
                                    .body(fromObject(new HttpErrorMessage(1025030,
                                            INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                            INTERNAL_SERVER_ERROR.getReasonPhrase())))
            );

    @Bean
    @Primary
    public AbstractErrorWebExceptionHandler exceptionHandler(
            ErrorAttributes errorAttributes,
            ResourceProperties resourceProperties,
            ApplicationContext applicationContext,
            ObjectProvider<ViewResolver> viewResolvers,
            ServerCodecConfigurer serverCodecConfigurer) {

        var exceptionHandler = new CommonExceptionHandler(errorAttributes,
                resourceProperties, applicationContext, EXCEPTION_MAPPINGS);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }
}
