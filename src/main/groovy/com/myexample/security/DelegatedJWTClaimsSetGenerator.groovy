package com.myexample.security

import com.nimbusds.jwt.JWTClaimsSet
import io.micronaut.context.annotation.Replaces
import io.micronaut.runtime.ApplicationConfiguration
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.token.config.TokenConfiguration
import io.micronaut.security.token.jwt.generator.claims.ClaimsAudienceProvider
import io.micronaut.security.token.jwt.generator.claims.JWTClaimsSetGenerator
import io.micronaut.security.token.jwt.generator.claims.JwtIdGenerator

import javax.annotation.Nullable

/**
 * Replacing bean `JWTClaimsSetGenerator`
 * To add tenant related info in the authentication details
 */
@Replaces(bean = JWTClaimsSetGenerator.class)
class DelegatedJWTClaimsSetGenerator extends JWTClaimsSetGenerator {

    DelegatedJWTClaimsSetGenerator(TokenConfiguration tokenConfiguration,
                                   @Nullable JwtIdGenerator jwtIdGenerator,
                                   @Nullable ClaimsAudienceProvider claimsAudienceProvider,
                                   @Nullable ApplicationConfiguration applicationConfiguration) {
        super(tokenConfiguration, jwtIdGenerator, claimsAudienceProvider, applicationConfiguration)
    }

    @Override
    protected void populateWithUserDetails(JWTClaimsSet.Builder builder, UserDetails userDetails) {
        super.populateWithUserDetails(builder, userDetails)
        if (userDetails instanceof DelegatedUserDetails) {
            builder.claim("sampleData", ((DelegatedUserDetails)userDetails).sampleData)
        }
    }
}
