package com.myexample.security

import edu.umd.cs.findbugs.annotations.Nullable
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpHeaderValues
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.token.jwt.render.AccessRefreshToken
import io.micronaut.security.token.jwt.render.BearerTokenRenderer

import javax.inject.Singleton

/**
 * Replacing `BearerTokenRenderer` bean return customized AccessRefreshToken.
 *
 * @author Prasad
 */
@Singleton
@Replaces(bean = BearerTokenRenderer.class)
class DelegatedBearerTokenRenderer extends BearerTokenRenderer{
    private final String BEARER_TOKEN_TYPE = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER

    @Override
    AccessRefreshToken render(Integer expiresIn, String accessToken, @Nullable String refreshToken) {
        return new AccessRefreshToken(accessToken, refreshToken, BEARER_TOKEN_TYPE, expiresIn)
    }

    @Override
    AccessRefreshToken render(UserDetails userDetails, Integer expiresIn, String accessToken, @Nullable String refreshToken) {
        DelegatedUserDetails user = userDetails
        new DelegatedBearerAccessRefreshToken(user.getUsername(), user.getRoles(), user.sampleData,
                user.userId, expiresIn, accessToken, refreshToken, BEARER_TOKEN_TYPE)
    }
}
