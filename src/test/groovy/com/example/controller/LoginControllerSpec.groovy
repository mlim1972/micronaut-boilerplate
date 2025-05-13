package com.example.controller

import com.example.service.UserService
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.OK
import static io.micronaut.http.HttpStatus.UNAUTHORIZED
import static io.micronaut.http.MediaType.TEXT_PLAIN

// if transaction, calls to verify user will not find the user
// will get: Query produced no result
@MicronautTest(transactional = false)
class LoginControllerSpec extends Specification{
    final static String prefix = "lcpec"

    @Inject
    @Client("/")
    HttpClient client

    @Inject
    UserService userService

    void 'Accessing a secured URL without authenticating returns unauthorized'() {
        when:
        client.toBlocking().exchange(HttpRequest.GET('/').accept(TEXT_PLAIN))

        then:
        HttpClientResponseException e = thrown()
        e.status == UNAUTHORIZED
    }

    void "upon successful authentication, a JSON Web token is issued to the user"() {
        given:
        def index = UUID.randomUUID().toString()
        def props = [firstName: "John", lastName: "Doe",
                     username: "${prefix}-${index}.user@email.com".toString(),
                     password: "123456", notes: "This is a test"]
        def user = userService.saveUser(props)
        print("user: ${user}")
        userService.findAll().each { print("in loop 1... ${it}") }

        when: 'Login endpoint is called with valid credentials'
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(props.username, props.password)
        HttpRequest request = HttpRequest.POST('/login', creds)
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken)

        then: 'the endpoint can be accessed'
        rsp.status == OK

        when:
        BearerAccessRefreshToken bearerAccessRefreshToken = rsp.body()

        then:
        bearerAccessRefreshToken.username == props.username
        bearerAccessRefreshToken.accessToken

        and: 'the access token is a signed JWT'
        JWTParser.parse(bearerAccessRefreshToken.accessToken) instanceof SignedJWT

        when: 'passing the access token as in the Authorization HTTP Header with the prefix Bearer allows the user to access a secured endpoint'
        String accessToken = bearerAccessRefreshToken.accessToken
        HttpRequest requestWithAuthorization = HttpRequest.GET('/hello' )
                .accept(TEXT_PLAIN)
                .bearerAuth(accessToken)
        HttpResponse<String> response = client.toBlocking().exchange(requestWithAuthorization, String)

        then:
        response.status == OK
        response.body() == "Hello ${props.username}"
    }

    void 'Login with invalid credentials returns unauthorized'() {
        given:
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("invalid.user@email.com", "wrongpassword")

        when:
        HttpRequest request = HttpRequest.POST('/login', creds)
        client.toBlocking().exchange(request, BearerAccessRefreshToken)

        then:
        HttpClientResponseException e = thrown()
        e.status == UNAUTHORIZED
    }

    void 'Accessing a secured URL with an invalid token returns unauthorized'() {
        when:
        HttpRequest requestWithAuthorization = HttpRequest.GET('/hello')
                .accept(TEXT_PLAIN)
                .bearerAuth("invalid_jwt_token") // Provide an invalid token
        client.toBlocking().exchange(requestWithAuthorization, String)

        then:
        HttpClientResponseException e = thrown()
        e.status == UNAUTHORIZED
    }
}