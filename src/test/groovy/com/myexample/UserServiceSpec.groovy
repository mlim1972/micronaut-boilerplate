package com.myexample

import com.myexample.service.UserService
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class UserServiceSpec extends Specification {
    @Inject
    UserService userService



    void "save user"(){
        given:
        String email = "user1@company.com"
        String password = 'password'

        when:
        def user = userService.saveUser(email, password)

        then:
        user.id > 0
        user.email == email
        user.password != password
    }

    void "find user created"(){
        given:
        String email = "user4@company.com"
        String password = 'password'
        def curList = userService.findAll()

        when:
        def user = userService.saveUser(email, password)

        then:
        user.id > 0
        user.email == email
        user.password != password

        when:
        def users = userService.findAll()

        then:
        users.size() == curList.size() + 1

        when:
        def user2 = userService.findUserByEmail(email)

        then:
        user2
        user2.email == email
    }
}
