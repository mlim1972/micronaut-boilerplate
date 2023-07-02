package com.example.controller

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpMethod
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
class UserControllerSpec extends Specification{
    final static String prefix = "ucspec"
    @Inject
    @Client("/")
    HttpClient client

    void "test user creation"() {
        def index = UUID.randomUUID().toString()

        given:
        def props = [firstName: "${prefix}-${index}.fname".toString(), lastName: "${prefix}-${index}.lname".toString(),
                     username: "lname-${prefix}-${index}@email.com".toString(), password: "12345"]
        when:
        HttpRequest request = HttpRequest.create(HttpMethod.POST, '/users')
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(props)
        HttpResponse rsp = client.toBlocking().exchange(request, Argument.of(Map.class), Argument.of(Map.class))
        Map user = rsp.body()

        then:
        rsp.status == HttpStatus.OK
        user.id != null
        user.lastName == props.lastName
        user.firstName == props.firstName
        user.username == props.username
    }

    void "test user update"() {
        def index = UUID.randomUUID().toString()

        given:
        def props = [firstName: "${prefix}-${index}.fname".toString(), lastName: "${prefix}-${index}.lname".toString(),
                     username: "lname-${prefix}-${index}@email.com".toString(), password: "12345"]
        when:
        HttpRequest request = HttpRequest.create(HttpMethod.POST, '/users')
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(props)
        HttpResponse rsp = client.toBlocking().exchange(request, Argument.of(Map.class), Argument.of(Map.class))
        Map user = rsp.body()

        then:
        rsp.status == HttpStatus.OK
        user.id != null
        user.lastName == props.lastName
        user.firstName == props.firstName
        user.username == props.username

        when:
        Map newProp = [lastName: "${prefix}-${index}.lname22".toString()]
        request = HttpRequest.create(HttpMethod.PUT, "/users/${user.id}".toString())
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(newProp)
        rsp = client.toBlocking().exchange(request, Argument.of(Map.class), Argument.of(Map.class))
        Map user2 = rsp.body()

        then:
        rsp.status == HttpStatus.OK
        user2.id != null
        user2.lastName == newProp.lastName
        user2.firstName == props.firstName
        user2.username == props.username
    }

    void "test user creation and listing"() {
        def index = UUID.randomUUID().toString()

        given:
        def props = [firstName: "${prefix}-${index}.fname".toString(), lastName: "${prefix}-${index}.lname".toString(),
                     username: "lname-${prefix}-${index}@email.com".toString(), password: "12345"]
        when:
        HttpRequest request = HttpRequest.create(HttpMethod.POST, '/users')
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(props)
        HttpResponse rsp = client.toBlocking().exchange(request, Argument.of(Map.class), Argument.of(Map.class))
        Map user = rsp.body()

        then:
        user.id != null

        when:
        request = HttpRequest.create(HttpMethod.GET, "/users/${user.id}".toString())
                .accept(MediaType.APPLICATION_JSON_TYPE)
        rsp = client.toBlocking().exchange(request, Argument.of(Map.class), Argument.of(Map.class))
        Map user2 = rsp.body()

        then:
        user.id == user2.id
        user.firstName == user2.firstName
    }

    void "test user creation and deletion"() {
        def index = UUID.randomUUID().toString()

        given:
        def props = [firstName: "${prefix}-${index}.fname".toString(), lastName: "${prefix}-${index}.lname".toString(),
                     username: "lname-${prefix}-${index}@email.com".toString(), password: "12345"]
        when:
        HttpRequest request = HttpRequest.create(HttpMethod.POST, '/users')
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(props)
        HttpResponse rsp = client.toBlocking().exchange(request, Argument.of(Map.class), Argument.of(Map.class))
        Map user = rsp.body()

        then:
        user.id != null

        when:
        request = HttpRequest.create(HttpMethod.DELETE, "/users/${user.id}".toString())
                .accept(MediaType.APPLICATION_JSON_TYPE)
        rsp = client.toBlocking().exchange(request, Argument.of(Map.class), Argument.of(Map.class))

        then:
        rsp.status == HttpStatus.OK
    }
}
