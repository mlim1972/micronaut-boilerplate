package com.example.swagger

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class SwaggerUiSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "test swagger ui is available"() {
        when:
        HttpRequest request = HttpRequest.GET("/swagger-ui/index.html")
                .accept(MediaType.TEXT_HTML_TYPE)
        HttpResponse rsp = client.toBlocking().exchange(request, Argument.of(String.class))

        print("rsp: ${rsp.body()}")
        then:
        rsp.status == HttpStatus.OK
        rsp.body().contains("swagger-ui")
    }
}
