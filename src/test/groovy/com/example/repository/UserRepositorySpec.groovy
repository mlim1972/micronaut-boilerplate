package com.example.repository

import com.example.domain.User
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import jakarta.persistence.EntityManager

@MicronautTest
class UserRepositorySpec extends Specification{
    static final prefix = "urspec"

    @Inject
    UserRepository userRepository

    @Inject
    EntityManager entityManager

    void "test user update"(){
        when:
        def user = new User(firstName: "john", lastName: "doe", email: "${prefix}.john@email.com", password: "12345")
        def saved = userRepository.save(user)
        userRepository.flush() // Ensure changes are flushed to the database

        then:
        saved.id != null
        saved.version == 0L

        when:
        User user2 = userRepository.findById(saved.id).get()
        user2.lastName = "doe2"
        User user3 = userRepository.save(user2)

        then:
        user2.lastName == user3.lastName
        user2.lastName == "doe2"
        user3.version != 1L // UserRepository will not change the version
    }

    void "test user update with EntityManager"(){
        when:
        def user = new User(firstName: "john", lastName: "doe", email: "${prefix}.john2@email.com", password: "12345")
        def saved = userRepository.save(user)
        User user2 = entityManager.find(User.class, user.id)
        user2.lastName = "doe2"
        entityManager.persist(user2)
        User user3 = userRepository.findById(user.id).get()

        then:
        user3.version == 1L
    }
}
