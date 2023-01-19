package com.example.controller

import com.example.service.HelloService
import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.MediaType
import io.micronaut.security.annotation.Secured

import java.security.Principal

@CompileStatic
@Controller("/hello")
@Secured("isAuthenticated()")
class HelloController {

    // Auto-wired by the constructor
    HelloService helloService

    HelloController(HelloService helloService){
        this.helloService = helloService
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    String index(Principal principal) {
        helloService.helloMessage + principal.name
    }
}