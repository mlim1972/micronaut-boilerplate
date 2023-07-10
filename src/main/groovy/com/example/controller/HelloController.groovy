package com.example.controller

import com.example.service.HelloService
import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.MediaType
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

import java.security.Principal

@CompileStatic
@Controller("/hello")
@Secured(SecurityRule.IS_ANONYMOUS)
class HelloController {

    // Auto-wired by the constructor
    HelloService helloService

    HelloController(HelloService helloService){
        this.helloService = helloService
    }

    @Get("/protected")
    @Produces(MediaType.TEXT_PLAIN)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    String index(Principal principal) {
        helloService.helloMessage + principal.name
    }

    @Get()
    @Produces(MediaType.TEXT_PLAIN)
    String index() {
        helloService.helloMessage + "World!!!"
    }
}