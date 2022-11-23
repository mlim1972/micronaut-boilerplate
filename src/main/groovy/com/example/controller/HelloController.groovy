package com.example.controller

import com.example.service.HelloService
import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.MediaType

@CompileStatic
@Controller("/hello")
class HelloController {

    // Auto-wired by the constructor
    HelloService helloService

    HelloController(HelloService helloService){
        this.helloService = helloService
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    String index() {
        helloService.helloMessage
    }
}