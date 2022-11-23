package com.example.services.security

import io.micronaut.core.annotation.NonNull

import javax.validation.constraints.NotBlank

/**
 * Interface that outlines the necessary methods for password encoding
 */
interface PasswordEncoder {
    String encode(@NotBlank @NonNull String rawPassword)

    boolean matches(@NotBlank @NonNull String rawPassword,
                    @NotBlank @NonNull String encodedPassword)
}