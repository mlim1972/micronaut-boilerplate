package com.myexample.security

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

interface PasswordEncoder {
    String encode(@NotBlank @NotNull String rawPassword)

    boolean matches(@NotBlank @NotNull String rawPassword, @NotBlank @NotNull String encodedPassword)
}