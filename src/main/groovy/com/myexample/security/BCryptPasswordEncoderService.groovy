package com.myexample.security


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import javax.inject.Singleton
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Singleton
class BCryptPasswordEncoderService implements PasswordEncoder {
    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder()

    String encode(@NotBlank @NotNull String rawPassword) {
        delegate.encode(rawPassword)
    }

    @Override
    boolean matches(@NotBlank @NotNull String rawPassword, @NotBlank @NotNull String encodedPassword) {
        delegate.matches(rawPassword, encodedPassword)
    }
}