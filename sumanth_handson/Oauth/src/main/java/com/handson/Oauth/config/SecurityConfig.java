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
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/actuator/health", "/actuator/info", "/api/public/**").permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .requestMatchers("/api/employee/**").hasAnyRole("EMPLOYEE", "ADMIN")

                        .anyRequest().authenticated()

                )

                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }

    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            List<GrantedAuthority> authorities = new ArrayList<>();

            Object scope = jwt.getClaims().get("scope");
            if (scope instanceof String scopeValue) {
                for (String item : scopeValue.split(" ")) {
                    if (!item.isBlank()) {
                        authorities.add(new SimpleGrantedAuthority("SCOPE_" + item));
                    }
                }
            }

            Object realmAccess = jwt.getClaims().get("realm_access");
            if (realmAccess instanceof Map<?, ?> realmAccessMap) {
                Object roles = realmAccessMap.get("roles");
                if (roles instanceof Collection<?> roleValues) {
                    roleValues.stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
                            .map(SimpleGrantedAuthority::new)
                            .forEach(authorities::add);
                }
            }

            return authorities;
        }
    }
}
