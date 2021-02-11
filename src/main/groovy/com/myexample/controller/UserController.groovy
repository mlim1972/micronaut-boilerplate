package com.myexample.controller

import com.myexample.domain.User
import com.myexample.service.UserService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/user")
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
