package com.myexample.security

import io.micronaut.security.token.jwt.render.AccessRefreshToken

/**
 * Class to override BearerAccessRefreshToken
 * to add tenant related information in authentication response.
 *
 * Also returning user role as string instead of Collection.
 */
class DelegatedBearerAccessRefreshToken extends AccessRefreshToken{
    Long userId
    String username
    String sampleData
    List<String> roles

    /**
     * Necessary for JSON serialization.
     */
    DelegatedBearerAccessRefreshToken() { }

    /**
     *
     * @param username      A string e.g. admin
     * @param role          Collection of Strings e.g. ( [ROLE_USER, ROLE_ADMIN] )
     * @param sampleData    Extra info
     * @param expiresIn     Access Token expiration
     * @param accessToken   JWT token
     * @param refreshToken  JWT token
     * @param tokenType     Type of token
     */
    DelegatedBearerAccessRefreshToken(String username,
                                      Collection<String> roles,
                                      String sampleData,
                                      Long userId,
                                      Integer expiresIn,
                                      String accessToken,
                                      String refreshToken,
                                      String tokenType) {
        super(accessToken, refreshToken, tokenType, expiresIn)
        this.userId = userId
        this.username = username
        this.roles = roles.toList()
        this.sampleData = sampleData
    }
}
