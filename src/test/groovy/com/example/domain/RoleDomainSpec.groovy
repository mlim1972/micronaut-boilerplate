package com.example.domain

import com.example.repository.RoleRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class RoleDomainSpec extends Specification {

    @Inject
    RoleRepository roleRepository

    void "test role creation"() {
        given:
        def authority = "ROLE_ADMIN"
        def role = new Role(authority: authority)

        when:
        def savedRole = roleRepository.save(role)

        then:
        savedRole.id != null
        savedRole.version == 0L
        savedRole.authority == authority
    }

    void "test role update"() {
        given:
        def authority = "ROLE_USER"
        def role = new Role(authority: authority)
        def savedRole = roleRepository.save(role)
        def newAuthority = "ROLE_MODERATOR"

        when:
        savedRole.authority = newAuthority
        def updatedRole = roleRepository.update(savedRole)

        then:
        updatedRole.id == savedRole.id
        updatedRole.version == 0L
        updatedRole.authority == newAuthority
    }

    void "test role deletion"() {
        given:
        def authority = "ROLE_GUEST"
        def role = new Role(authority: authority)
        def savedRole = roleRepository.save(role)

        when:
        roleRepository.delete(savedRole)
        def retrievedRole = roleRepository.findById(savedRole.id).orElse(null)

        then:
        retrievedRole == null
    }
}