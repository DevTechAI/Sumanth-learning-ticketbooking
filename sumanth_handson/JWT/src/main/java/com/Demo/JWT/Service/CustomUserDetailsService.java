package com.Demo.JWT.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (!username.equals("sumanth")) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username("sumanth")
                .password("{noop}password123")
                .roles("USER")
                .build();
    }
}