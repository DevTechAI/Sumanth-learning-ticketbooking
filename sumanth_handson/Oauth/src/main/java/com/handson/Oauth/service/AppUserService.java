package com.handson.Oauth.service;

import com.handson.Oauth.entity.AppUser;
import com.handson.Oauth.entity.Role;
import com.handson.Oauth.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public AppUser createEmployee(String username, String displayName, String email) {
        AppUser employee = new AppUser(username, displayName, email, Set.of(Role.ROLE_EMPLOYEE));
        return appUserRepository.save(employee);
    }
}
