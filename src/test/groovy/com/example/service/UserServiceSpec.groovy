package com.example.service

import com.example.domain.User
import io.micronaut.data.model.Page
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class UserServiceSpec extends Specification{
    static final prefix = "usspec"

    @Inject
    UserService userService

    /**
     * Test saving users using the UserService
     */
    void "test saving user"() {
        when:
        def props = [firstName: "John", lastName: "Doe", email: "${prefix}.john@email.com",
                     password: "123456", notes: "This is a test"]
        def user = userService.saveUser(props)

        then:
        user != null
        user.id != null
        user.version == 0
        user.dateCreated != null
        user.dateUpdated != null
        user.enabled
    }

    /**
     * Test saving and updating a user. It also shows that that the version property is updated as part
     * of the update. This indicates that optimistic locking is working as well.
     */
    void "test saving and updating user"() {
        when:
        def props = [firstName: "John2", lastName: "Doe2", email: "${prefix}.john2@email.com",
                     password: "1234567890", notes: "This is a test2"]
        def user = userService.saveUser(props)

        then:
        user != null
        user.id != null
        user.version == 0
        user.dateCreated != null
        user.dateUpdated != null
        user.enabled

        when:
        println("user.version = ${user.version}")
        def changeProps = [firstName: "john2.2"]
        def user2 = userService.updateUser(user.id, changeProps)
        println("user.version = ${user.version}")
        println("user.firstName = ${user.firstName}")

        then:
        user.id == user2.id
        user.version + 1 == user2.version
        user.firstName != user2.firstName
        user2.firstName == "john2.2"
        user.lastName == user2.lastName
        user.lastName == "Doe2"
    }

    /**
     * Saving user with non-detached. This means that the user reference is handled by the hibernate context
     */
    void "test saving non-detached user"(){
        when:
        def props = [firstName: "John", lastName: "Doe", email: "${prefix}.john3@email.com",
                     password: "123456", notes: "This is a test"]
        def user = userService.saveUser(props, false)

        then:
        user.version == 0L

        when:
        def user2 = userService.updateUser(user.id, [firstName: "Johny"])

        then:
        user.id == user2.id
        user.firstName == user2.firstName
        user.firstName == "Johny"
        user.version == user2.version
        user2.version == 1L

    }

    void "test save and delete user"(){
        when:
        def props = [firstName: "John", lastName: "Doe", email: "${prefix}.john3@email.com",
                     password: "123456", notes: "This is a test"]
        def user = userService.saveUser(props, false)

        then:
        user.id != null
        user.firstName == "John"

        when:
        userService.deleteUser(user.id)
        def user2 = userService.getUser(user.id)

        then:
        user2 == null

    }

    void "test listing users"(){
        given:
        def data = []
        for(i in 0..10){
            def props = [firstName: "fname$i", lastName: "lname$i",
                         email: "fname${i}.lname${i}@email.com", password: "12345.$i"]
            data << props
        }

        when:
        def users = []
        data.forEach{
            users << userService.saveUser(it)
        }
        Page<User> dbUsers = userService.getUsers(0,5)

        then:
        dbUsers.size == 5

    }
}
