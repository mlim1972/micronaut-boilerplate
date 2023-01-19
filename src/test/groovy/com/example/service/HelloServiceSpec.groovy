package com.example.service


import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class HelloServiceSpec extends Specification {

    @Inject
    HelloService helloService

    void "test hello world response"() {
        when:
        def hello = helloService.helloMessage

        then:
        hello == "Hello "
    }
}