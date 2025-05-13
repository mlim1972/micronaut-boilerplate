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

    void "test add role"() {
        when:
        def role = roleService.addRole("AnotherTestRole")

        then:
        role.id != null
        role.version == 0L
        role.authority == "AnotherTestRole"
    }

    void "test add roles"() {
        when:
        def roleNames = ["Role1", "Role2", "Role3"]
        def roles = roleService.addRoles(roleNames as ArrayList)

        then:
        roles.size() == 3
        roles*.authority == roleNames
        roles.every { it.id != null }
    }
}