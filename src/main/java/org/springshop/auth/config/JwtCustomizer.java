package org.springshop.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.stream.Collectors;

@Configuration
public class JwtCustomizer {

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> doCustomize() {
        return context -> {
            if (context.getPrincipal() != null && context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                String roles = context.getPrincipal()
                        .getAuthorities()
                        .stream()
                        .map(authority -> "ROLE_" + authority.getAuthority().toUpperCase())
                        .collect(Collectors.joining(","));

                context.getClaims()
                        .claim("roles", roles);
            }

        };
    }
}
