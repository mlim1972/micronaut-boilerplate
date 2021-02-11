package com.myexample

import com.myexample.service.UserService
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class UserServiceSpec extends Specification {
    @Inject
    UserService userService

    String email = "user1@company.com"
    String password = 'password'

    void "save user"(){
        when:
        def user = userService.saveUser(email, password)

        then:
        user.id > 0
        user.email == email
        user.password == password
    }

    void "find user created"(){
        when:
        def user = userService.saveUser(email, password)

        then:
        user.id > 0
        user.email == email
        user.password == password

        when:
        def users = userService.findAll()

        then:
        users.size() == 1
        users[0].email == email
    }
}
