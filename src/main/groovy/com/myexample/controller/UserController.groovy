package com.myexample.controller

import com.myexample.domain.User
import com.myexample.service.UserService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/user")
@Secured(SecurityRule.IS_ANONYMOUS)
class UserController {

    UserService userService

    UserController(UserService userService){
        this.userService = userService
    }

    @Get
    List<User> index() {
        userService.findAll()
    }
}
