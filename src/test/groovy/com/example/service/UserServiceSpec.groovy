package com.example.service

import com.example.domain.Role
import com.example.domain.User
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class UserServiceSpec extends Specification{
    static final prefix = "usspec"

    @Inject
    UserService userService

    @Inject
    RoleService roleService

    /**
     * Test saving users using the UserService
     */
    void "test saving user"() {
        def index = UUID.randomUUID().toString()
        when:
        def props = [firstName: "John", lastName: "Doe",
                     username: "${prefix}-${index}.john@email.com".toString(),
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
        def index = UUID.randomUUID().toString()

        when:
        def props = [firstName: "John2", lastName: "Doe2",
                     username: "${prefix}-${index}.john2@email.com".toString(),
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
    void "test saving non detached user"() {
        def index = UUID.randomUUID().toString()

        when:
        def props = [firstName: "John", lastName: "Doe",
                     username: "${prefix}-${index}.john3@email.com".toString(),
                     password: "123456", notes: "This is a test"]
        def user = userService.saveUser(props, false)

        then:
        user.version == 0L

        when:
        def user2 = userService.updateUser(user.id, [firstName: "Johny"], false)

        then:
        user.id == user2.id
        user.firstName == user2.firstName
        user.firstName == "Johny"
        user.version == user2.version
        user2.version == 1L
    }

    void "test save and delete user"(){
        def index = UUID.randomUUID().toString()

        when:
        def props = [firstName: "John", lastName: "Doe",
                     username: "${prefix}-${index}.john4@email.com".toString(),
                     password: "123456", notes: "This is a test"]
        def user = userService.saveUser(props)

        then:
        user.id != null
        user.firstName == "John"

        when:
        userService.deleteUser(user.id)
        def user2 = userService.getUser(user.id)

        then:
        user2 == null

    }

    void "test save and find by username"(){
        def index = UUID.randomUUID().toString()

        when:
        def props = [firstName: "John", lastName: "Doe",
                     username: "${prefix}-${index}.john5@email.com".toString(),
                     password: "123456", notes: "This is a test"]
        def user = userService.saveUser(props)

        then:
        user.id != null
        user.firstName == "John"

        when:
        def user2 = userService.findByUsername(props.username)

        then:
        user2.id == user.id

    }

    void "test listing users"(){
        def index = new Date().getTime()

        when:
        given:
        def data = []
        for(i in 0..10){
            def props = [firstName: "fname$i", lastName: "lname$i",
                         username: "user.${index}.${i}@domain${prefix}.com".toString(), password: "12345.$i"]
            data << props
        }

        when:
        def users = []
        data.forEach{
            users << userService.saveUser(it)
        }
        List<User> dbUsers = userService.getUsers(1,5)

        then:
        dbUsers.size() == 5
    }

    void "test saving user with roles"(){
        def index = UUID.randomUUID().toString()

        when:
        def role = roleService.addRole("TestRole")
        def props = [firstName: "John", lastName: "Doe",
                     username: "${prefix}-${index}.john7@email.com".toString(),
                     password: "123456", notes: "This is a test", roles: [role]]
        def user = userService.saveUser(props)

        print(user)

        then:
        user.id != null
        user.firstName == props.firstName
        user.roles.size() == 1
        ((Role)user.roles[0]).authority == role.authority
    }

    void "test updating user roles"() {
        def index = UUID.randomUUID().toString()

        given:
        def role1 = roleService.addRole("RoleA")
        def role2 = roleService.addRole("RoleB")
        def props = [firstName: "John", lastName: "Doe",
                     username: "${prefix}-${index}.john8@email.com".toString(),
                     password: "123456", notes: "This is a test", roles: [role1]]
        def user = userService.saveUser(props)

        when:
        def updateProps = [roles: [role2]]
        def updatedUser = userService.updateUser(user.id, updateProps)

        then:
        updatedUser.roles.size() == 1
        ((Role)updatedUser.roles[0]).authority == role2.authority
    }

    void "test removing roles from user"() {
        def index = UUID.randomUUID().toString()

        given:
        def role1 = roleService.addRole("RoleX")
        def role2 = roleService.addRole("RoleY")
        def props = [firstName: "John", lastName: "Doe",
                     username: "${prefix}-${index}.john9@email.com".toString(),
                     password: "123456", notes: "This is a test", roles: [role1, role2]]
        def user = userService.saveUser(props)

        when:
        def updateProps = [roles: []]
        def updatedUser = userService.updateUser(user.id, updateProps)

        then:
        updatedUser.roles.size() == 0
    }
}