package com.handson.Oauth.config;

import com.handson.Oauth.entity.AppUser;
import com.handson.Oauth.entity.Role;
import com.handson.Oauth.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(AppUserRepository appUserRepository) {
        return args -> {
            createIfMissing(appUserRepository, "admin", "Admin User", "admin@example.com", Set.of(Role.ROLE_ADMIN));
            createIfMissing(appUserRepository, "employee", "Employee User", "employee@example.com", Set.of(Role.ROLE_EMPLOYEE));
        };
    }

    private void createIfMissing(
            AppUserRepository appUserRepository,
            String username,
            String displayName,
            String email,
            Set<Role> roles
    ) {
        appUserRepository.findByUsername(username)
                .orElseGet(() -> appUserRepository.save(new AppUser(username, displayName, email, roles)));
    }
}
