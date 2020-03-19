package com.cwd.tg.ugc.configuration;

import com.cwd.tg.ugc.security.SecurityTokenBasedAuthenticationManager;
import com.cwd.tg.ugc.security.TokenAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            AuthenticationWebFilter authenticationWebFilter,
            ServerHttpSecurity http) {
        return http
                .requestCache().disable()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(
            SecurityTokenBasedAuthenticationManager authenticationManager,
            TokenAuthenticationConverter tokenAuthenticationConverter) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);

        authenticationWebFilter.setServerAuthenticationConverter(tokenAuthenticationConverter);
        authenticationWebFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());

        return authenticationWebFilter;
    }
}
