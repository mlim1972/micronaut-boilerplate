package com.example.controller

import com.example.client.AppClient
import com.example.domain.User
import com.example.service.UserService
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification


// Setting the transaction to false allows the userService to save the user
// and test that the user is saved from the same spock test
@MicronautTest(transactional = false)
class DeclarativeHttpClientWithJwtSpec extends Specification{
    final static String prefix = "dhcwjspec"
    final static String plainPassword = "123456"

    @Inject
    AppClient appClient

    @Inject
    UserService userService

    void "Verify JWT authentication works with declarative @Client"() {
        given:
        def index = UUID.randomUUID().toString()
        def username = "${prefix}-${index}.email@email.com".toString()
        def password = plainPassword.toString()
        println("username: $username; password: $password")

        def props = [firstName: "${prefix}-${index}.fname".toString(),
                     lastName: "${prefix}-${index}.lname".toString(),
                     username: username, password: password]

        when:
        def mainUser = userService.saveUser(props)

        then:
        mainUser instanceof User
        mainUser.id != null

        when: 'Login endpoint is called with valid credentials'
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password)
        BearerAccessRefreshToken loginRsp = appClient.login(creds)

        then:
        loginRsp
        loginRsp.accessToken
        JWTParser.parse(loginRsp.accessToken) instanceof SignedJWT

        when:
        String msg = appClient.home("Bearer $loginRsp.accessToken")

        then:
        msg == "Hello ${mainUser.username}"

        when:
        userService.deleteUser(mainUser.id)
        then:
        userService.findByUsername(mainUser.username) == null
    }
}
