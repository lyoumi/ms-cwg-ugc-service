package com.cwd.tg.ugc.errors.handlers;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import com.cwd.tg.ugc.errors.http.HttpErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class CommonExceptionHandler extends AbstractErrorWebExceptionHandler {

    private static final String REQUEST_ID = "request_id";

    private final Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> exceptionMappings;

    public CommonExceptionHandler(ErrorAttributes errorAttributes,
            ResourceProperties resourceProperties,
            ApplicationContext applicationContext,
            Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> exceptionMappings) {
        super(errorAttributes, resourceProperties, applicationContext);
        this.exceptionMappings = exceptionMappings;
    }

    @Override
    public RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), request -> renderErrorResponse(request, errorAttributes));
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request, ErrorAttributes errorAttributes) {
        var exception = errorAttributes.getError(request);
        log.error("Exception occurred: request_id: {}", request.headers().asHttpHeaders().get(REQUEST_ID), exception);
        return Optional.ofNullable(exceptionMappings.get(exception.getClass()))
                .map(throwableMonoFunction -> throwableMonoFunction.apply(exception))
                .orElse(ServerResponse.status(INTERNAL_SERVER_ERROR)
                        .body(fromObject(new HttpErrorMessage(1005000,
                                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                INTERNAL_SERVER_ERROR.getReasonPhrase()))));
    }

}
