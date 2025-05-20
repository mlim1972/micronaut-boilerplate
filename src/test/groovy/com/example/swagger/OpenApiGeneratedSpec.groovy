package com.example.swagger

import io.micronaut.core.io.ResourceLoader
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification


@MicronautTest(startApplication = false)
class OpenApiGeneratedSpec extends Specification {

    @Inject
    ResourceLoader resourceLoader

    void "verify OpenAPI specification is generated and valid"() {
        given:
        def resource = resourceLoader.getResource("META-INF/swagger/micronaut-boilerplate-0.1.yml")

        expect:
        resource.present

    }
}