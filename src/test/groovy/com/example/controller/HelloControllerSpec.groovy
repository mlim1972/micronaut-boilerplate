package com.example.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest
class HelloControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "test hello world response"() {
        when:
        HttpRequest request = HttpRequest.GET('/hello')
        String rsp = client.toBlocking().retrieve(request)

        then:
        HttpClientResponseException e = thrown()
        e.status == UNAUTHORIZED
    }
}