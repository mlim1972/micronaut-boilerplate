package com.example.client

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import static io.micronaut.http.HttpHeaders.AUTHORIZATION

@CompileStatic
@Client("/")
interface AppClient {

    @Post("/login")
    BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials)

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/hello/protected")
    String home(@Header(AUTHORIZATION) String authorization)
}