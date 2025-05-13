package com.example.repository

import com.example.domain.Role
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class RoleRepositorySpec extends Specification {

    @Inject
    RoleRepository roleRepository

    void "test role creation and retrieval"() {
        given:
        def roleName = "ROLE_ADMIN"
        def role = new Role(authority: roleName)

        when:
        def savedRole = roleRepository.save(role)
        def retrievedRole = roleRepository.findById(savedRole.id).orElse(null)

        then:
        savedRole.id != null
        retrievedRole != null
        retrievedRole.authority == roleName
    }

    void "test role update"() {
        given:
        def role = new Role(authority: "ROLE_USER")
        def savedRole = roleRepository.save(role)
        def newRoleName = "ROLE_MODERATOR"

        when:
        savedRole.authority = newRoleName
        def updatedRole = roleRepository.update(savedRole)
        def retrievedRole = roleRepository.findById(updatedRole.id).orElse(null)

        then:
        retrievedRole != null
        retrievedRole.authority == newRoleName
    }

    void "test role deletion"() {
        given:
        def role = new Role(authority: "ROLE_GUEST")
        def savedRole = roleRepository.save(role)

        when:
        roleRepository.delete(savedRole)
        def retrievedRole = roleRepository.findById(savedRole.id).orElse(null)

        then:
        retrievedRole == null
    }

    void "test find all roles"() {
        given:
        def role1 = new Role(authority: "ROLE_ONE")
        def role2 = new Role(authority: "ROLE_TWO")
        roleRepository.saveAll([role1, role2])

        when:
        def allRoles = roleRepository.findAll()

        then:
        allRoles.size() >= 2
        allRoles.any { it.authority == "ROLE_ONE" }
        allRoles.any { it.authority == "ROLE_TWO" }
    }
}