package com.handson.Oauth.dto;

import com.handson.Oauth.entity.AppUser;
import com.handson.Oauth.entity.Role;

import java.util.Set;

public record AppUserResponse(
        Long id,
        String username,
        String displayName,
        String email,
        Set<Role> roles
) {
    public static AppUserResponse from(AppUser user) {
        return new AppUserResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
