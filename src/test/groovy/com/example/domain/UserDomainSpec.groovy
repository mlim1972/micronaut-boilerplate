package com.example.domain

import com.example.repository.UserRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class UserDomainSpec extends Specification {

    @Inject
    UserRepository userRepository

    void "test dateCreated is set on save"() {
        given:
        User user = new User(
                lastName: "Test",
                email: "test-${System.currentTimeMillis()}@example.com", // Ensure unique email
                password: "password"
                // enabled defaults to true
        )

        when:
        User savedUser = userRepository.save(user)
        userRepository.flush() // Ensure changes are flushed to the database

        then:
        savedUser.id != null
        savedUser.dateCreated != null // <<< THE CRITICAL CHECK
        savedUser.dateUpdated != null // Should also be set on initial save

        // Optional: Clean up
        // userRepository.delete(savedUser)
        println "Saved User: ${savedUser}" // Print to see the object state
    }
}
