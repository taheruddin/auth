package org.springshop.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springshop.auth.util.Jks;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthWithPkce {

    private final Resource jks;
    private final String keystorePassword;
    private final String keyAlias;

    public AuthWithPkce(
            @Value("classpath:jks/keystore.jks") Resource jks,
            @Value("${jks.password}") String keystorePassword,
            @Value("${jks.alias}") String keyAlias) {
        this.jks = jks;
        this.keystorePassword = keystorePassword;
        this.keyAlias = keyAlias;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorrizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(
                        authorizationServerConfigurer,
                        authorizationServer -> authorizationServer.oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
                )
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        // Redirect to the login page when not authenticated from the authorization endpoint
        http.exceptionHandling(
                (exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
        );

        // TODO: CORS for the authorization server end pints
        http.cors(
                cors -> cors.configurationSource(new CorsConfigurationSourceImpl())
        );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // TODO: CORS for the form login and logout endpoint
        http.cors(
                cors -> cors.configurationSource(new CorsConfigurationSourceImpl())
        );

        http.authorizeHttpRequests(
                requestMatcherRegistry -> requestMatcherRegistry
                        .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(2))
                .refreshTokenTimeToLive(Duration.ofDays(5))
                .build();

        // PKCE client configuration
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("pkce-oidc-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // .redirectUri("https://oauth.pstmn.io/v1/callback") // Enable this for Postman
                .redirectUri("http://localhost:5173/oauth-callback") // Enable this for the frontend react app
                .postLogoutRedirectUri("http://localhost:5173/") // For the frontend react app
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenSettings(tokenSettings)
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jks.toRsa(jks, keystorePassword, keyAlias);
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
