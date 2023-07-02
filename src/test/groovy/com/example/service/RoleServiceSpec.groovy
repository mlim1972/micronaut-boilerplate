package com.example.service

import com.example.domain.Role
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class RoleServiceSpec extends Specification {
    @Inject
    RoleService roleService

    void "test role save"() {
        when:
        def role = new Role(authority: "TestRole")
        def saved = roleService.save(role)

        then:
        saved.id != null
        saved.version == 0L
        saved.authority == "TestRole"
    }
}
