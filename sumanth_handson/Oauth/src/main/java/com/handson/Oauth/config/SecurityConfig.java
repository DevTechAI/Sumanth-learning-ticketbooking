package com.handson.Oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // These endpoints do not require a token
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/api/public/**"
                        ).permitAll()

                        // Requires Okta ADMIN group
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Requires Okta EMPLOYEE or ADMIN group
                        .requestMatchers("/api/employee/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        // Any other endpoint requires a valid token
                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(
                new OktaGroupsConverter()
        );

        /*
         * Optional:
         * Use a readable claim as the authenticated username.
         *
         * Okta access tokens commonly contain "sub".
         * Depending on your claim configuration, you could use:
         * preferred_username, email, or sub.
         */
        authenticationConverter.setPrincipalClaimName("sub");

        return authenticationConverter;
    }

    static class OktaGroupsConverter
            implements Converter<Jwt, Collection<GrantedAuthority>> {

        private final JwtGrantedAuthoritiesConverter scopeConverter =
                new JwtGrantedAuthoritiesConverter();

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {

            List<GrantedAuthority> authorities = new ArrayList<>();

            /*
             * Extract OAuth scopes.
             *
             * Example:
             * "scp": ["openid", "profile", "email"]
             *
             * These become:
             * SCOPE_openid
             * SCOPE_profile
             * SCOPE_email
             */
            Collection<GrantedAuthority> scopeAuthorities =
                    scopeConverter.convert(jwt);

            if (scopeAuthorities != null) {
                authorities.addAll(scopeAuthorities);
            }

            /*
             * Extract Okta groups.
             *
             * Example JWT claim:
             * "groups": ["ADMIN", "EMPLOYEE"]
             *
             * These become:
             * ROLE_ADMIN
             * ROLE_EMPLOYEE
             */
            Object groupsClaim = jwt.getClaims().get("groups");

            if (groupsClaim instanceof Collection<?> groups) {

                groups.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .filter(group -> !group.isBlank())
                        .map(String::toUpperCase)
                        .map(group ->
                                group.startsWith("ROLE_")
                                        ? group
                                        : "ROLE_" + group
                        )
                        .map(SimpleGrantedAuthority::new)
                        .forEach(authorities::add);
            }

            return authorities;
        }
    }
}