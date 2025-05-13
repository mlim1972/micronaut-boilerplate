package com.example.service.security

import groovy.transform.CompileStatic
import jakarta.inject.Singleton
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder as SpringSecurityPasswordEncoder

/**
 * This Service helps with the password encoding. This is a one way hashing
 */
@CompileStatic
@Singleton
class BCryptPasswordEncoderService implements PasswordEncoder {
    SpringSecurityPasswordEncoder delegate = new BCryptPasswordEncoder()

    /**
     * Encodes a cleared text password
     * @param rawPassword
     * @return the encoded password
     */
    @Override
    String encode(@NotBlank @NotNull String rawPassword) {
        delegate.encode(rawPassword)
    }

    /**
     * This method compares a plain text password with an already encoded password.
     * @param rawPassword the plain text password
     * @param encodedPassword the encoded password
     * @return a boolean identifying if the password are the same or not
     */
    @Override
    boolean matches(@NotBlank @NotNull String rawPassword,
                    @NotBlank @NotNull String encodedPassword) {
        delegate.matches(rawPassword, encodedPassword)
    }
}