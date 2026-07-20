package com.handson.Oauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateEmployeeRequest(
        @NotBlank String username,
        @NotBlank String displayName,
        @Email @NotBlank String email
) {
}
