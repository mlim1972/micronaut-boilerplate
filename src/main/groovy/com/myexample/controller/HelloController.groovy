package com.myexample.controller

import com.myexample.service.HelloService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/hello")
class HelloController {

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