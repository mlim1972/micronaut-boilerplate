package com.myexample.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class HelloControllerTest extends Specification {

    @Inject
    @Client("/")
    RxHttpClient client

    void "Test the hello controller return value"() {
        when:
        HttpRequest<String> request = HttpRequest.GET("/hello")
        String body = client.toBlocking().retrieve(request)

        then:
        body
        body == "Hello World"

    }
}