package com.cwd.tg.ugc.handlers;

import com.cwd.tg.ugc.security.User;
import reactor.core.publisher.Mono;

public interface AccountRequestHandler {
    Mono<User> getUserDetails();
}
