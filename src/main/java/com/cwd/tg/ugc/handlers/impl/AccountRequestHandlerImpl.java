package com.cwd.tg.ugc.handlers.impl;

import com.cwd.tg.ugc.handlers.AccountRequestHandler;
import com.cwd.tg.ugc.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AccountRequestHandlerImpl implements AccountRequestHandler {

    @Override
    public Mono<User> getUserDetails() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(User.class);
    }
}
