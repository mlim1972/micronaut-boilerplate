package com.example.service.security

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


/**
 * Interface that outlines the necessary methods for password encoding
 */
interface PasswordEncoder {
    String encode(@NotBlank @NotNull String rawPassword)

    boolean matches(@NotBlank @NotNull String rawPassword,
                    @NotBlank @NotNull String encodedPassword)
}