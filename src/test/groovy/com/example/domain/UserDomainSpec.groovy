package com.example.domain

import com.example.repository.UserRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.persistence.PersistenceException
import spock.lang.Specification

@MicronautTest
class UserDomainSpec extends Specification {
    static final prefix = "udspec"

    @Inject
    UserRepository userRepository

    void "test user creation"() {
        given:
        def firstName = "John"
        def lastName = "Doe"
        def username = "${prefix}.john.doe@example.com"
        def password = "password123"
        def user = new User(firstName: firstName, lastName: lastName, username: username, password: password)

        when:
        def savedUser = userRepository.save(user)

        then:
        savedUser.id != null
        savedUser.version == 0L
        savedUser.firstName == firstName
        savedUser.lastName == lastName
        savedUser.username == username
    }

    void "test user deletion"() {
        given:
        def user = new User(firstName: "Jane", lastName: "Smith", username: "${prefix}.jane.smith@example.com", password: "securePass")
        def savedUser = userRepository.save(user)

        when:
        userRepository.delete(savedUser)
        def retrievedUser = userRepository.findById(savedUser.id).orElse(null)

        then:
        retrievedUser == null
    }

    void "test unique username constraint"() {
        given:
        def username = "${prefix}.unique.user@example.com"
        def user1 = new User(firstName: "User", lastName: "One", username: username, password: "pass1")
        userRepository.save(user1)

        when:
        def user2 = new User(firstName: "User", lastName: "Two", username: username, password: "pass2")
        userRepository.save(user2)

        then:
        thrown(PersistenceException)
    }
}