package com.cwd.tg.ugc.clients;

import com.cwd.tg.ugc.security.User;
import reactor.core.publisher.Mono;

public interface AuthWebClient {
    Mono<User> validateUserToken(String token, String requestId);
}
