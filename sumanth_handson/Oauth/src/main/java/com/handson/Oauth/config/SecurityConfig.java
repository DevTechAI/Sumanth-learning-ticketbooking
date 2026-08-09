package com.handson.Oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter
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

                        // Requires Auth0 ADMIN role, permission, or trusted admin client
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Requires Auth0 EMPLOYEE or ADMIN group
                        .requestMatchers("/api/employee/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        // Any other endpoint requires a valid token
                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter
                                )
                        )
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            String message = authException.getMessage()
                                    .replace("\\", "\\\\")
                                    .replace("\"", "\\\"");
                            response.getWriter().write("""
                                    {"error":"unauthorized","message":"%s"}
                                    """.formatted(message));
                        })
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(
            @Value("${auth0.admin-client-id}") String adminClientId
    ) {

        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(
                new Auth0AuthoritiesConverter(adminClientId)
        );

        /*
         * Optional:
         * Use a readable claim as the authenticated username.
         *
         * Auth0 access tokens commonly contain "sub".
         * Depending on your claim configuration, you could use:
         * preferred_username, email, or sub.
         */
        authenticationConverter.setPrincipalClaimName("sub");

        return authenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            @Value("${auth0.audience}") String audience
    ) {
        return new LazyAuth0JwtDecoder(issuerUri, audience);
    }

    static class LazyAuth0JwtDecoder implements JwtDecoder {

        private final String issuerUri;
        private final String audience;
        private volatile JwtDecoder delegate;

        LazyAuth0JwtDecoder(String issuerUri, String audience) {
            this.issuerUri = issuerUri;
            this.audience = audience;
        }

        @Override
        public Jwt decode(String token) {
            return getDelegate().decode(token);
        }

        private JwtDecoder getDelegate() {
            JwtDecoder current = delegate;

            if (current == null) {
                synchronized (this) {
                    current = delegate;

                    if (current == null) {
                        current = createDelegate();
                        delegate = current;
                    }
                }
            }

            return current;
        }

        private JwtDecoder createDelegate() {
            NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                    .withIssuerLocation(issuerUri)
                    .build();

            OAuth2TokenValidator<Jwt> issuerValidator =
                    JwtValidators.createDefaultWithIssuer(issuerUri);
            OAuth2TokenValidator<Jwt> audienceValidator =
                    new AudienceValidator(audience);

            jwtDecoder.setJwtValidator(
                    new DelegatingOAuth2TokenValidator<>(
                            issuerValidator,
                            audienceValidator
                    )
            );

            return jwtDecoder;
        }
    }

    static class AudienceValidator implements OAuth2TokenValidator<Jwt> {

        private final String audience;

        AudienceValidator(String audience) {
            this.audience = audience;
        }

        @Override
        public OAuth2TokenValidatorResult validate(Jwt jwt) {
            if (jwt.getAudience().contains(audience)) {
                return OAuth2TokenValidatorResult.success();
            }

            OAuth2Error error = new OAuth2Error(
                    "invalid_token",
                    "The required audience is missing",
                    null
            );
            return OAuth2TokenValidatorResult.failure(error);
        }
    }

    static class Auth0AuthoritiesConverter
            implements Converter<Jwt, Collection<GrantedAuthority>> {

        private final JwtGrantedAuthoritiesConverter scopeConverter =
                new JwtGrantedAuthoritiesConverter();
        private final String adminClientId;

        Auth0AuthoritiesConverter(String adminClientId) {
            this.adminClientId = adminClientId;
        }

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

            addRolesFromClaim(authorities, jwt, "groups");
            addRolesFromClaim(authorities, jwt, "roles");
            addPermissions(authorities, jwt);
            addAdminRoleForTrustedClient(authorities, jwt);

            return authorities;
        }

        private void addRolesFromClaim(
                List<GrantedAuthority> authorities,
                Jwt jwt,
                String claimName
        ) {
            Object claim = jwt.getClaims().get(claimName);

            if (claim instanceof Collection<?> values) {
                values.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .map(String::trim)
                        .filter(value -> !value.isBlank())
                        .map(String::toUpperCase)
                        .map(value -> value.startsWith("ROLE_") ? value : "ROLE_" + value)
                        .map(SimpleGrantedAuthority::new)
                        .forEach(authorities::add);
            }
        }

        private void addPermissions(List<GrantedAuthority> authorities, Jwt jwt) {
            Object permissionsClaim = jwt.getClaims().get("permissions");

            if (permissionsClaim instanceof Collection<?> permissions) {
                permissions.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .map(String::trim)
                        .filter(permission -> !permission.isBlank())
                        .map(permission -> "SCOPE_" + permission)
                        .map(SimpleGrantedAuthority::new)
                        .forEach(authorities::add);
            }
        }

        private void addAdminRoleForTrustedClient(
                List<GrantedAuthority> authorities,
                Jwt jwt
        ) {
            if (adminClientId.isBlank()) {
                return;
            }

            Map<String, Object> claims = jwt.getClaims();
            Object authorizedParty = claims.get("azp");
            Object grantType = claims.get("gty");

            if (adminClientId.equals(authorizedParty)
                    && "client-credentials".equals(grantType)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }
    }
}
