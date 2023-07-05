package com.example.controller

import com.example.domain.User
import com.example.service.UserService
import groovy.util.logging.Slf4j
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured

/**
 * This is the controller endpoint for User
 */
@ExecuteOn(TaskExecutors.IO)
@Controller("/users")
@Secured("isAnonymous()")
@Slf4j
class UserController {
    UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    /**
     * Endpoint for /users using GET. This endpoint uses pagination by
     * providing the page number and size of the page
     * @param page the page number for the pagination
     * @param size the size of the page for the pagination
     * @return a list of users
     *
     * Why is this not working? This makes more sense that the one that works!
     * List<User> getUsers(@Nullable @QueryValue Integer start=0, @Nullable @QueryValue Integer end=10)
     */
    @Get
    List<User> getUsers(@Nullable @QueryValue(defaultValue = "1") Integer page,
                        @Nullable @QueryValue(defaultValue = "10") Integer size) {
        if(page < 1) page = 1
        if(size < 1 || size > 100) size = 10

        log.info("page=${page}; size=${size}")

        userService.getUsers(page, size).toList()
    }

    /**
     * Endpoint for /users/id to retrieve a particular user by passing the user id
     * @param id is the key for retrieving the user object
     * @return a user object
     */
    @Get("/{id}")
    User getUser(Long id) {
        userService.getUser(id)
    }

    /**
     * Endpoint for /users/id with PUT to update the user based on the user id
     * @param id the primary key to identify the user to update
     * @param props properties for the user to update. Only changed properties need
     * to be sent and not the whole User's property
     * @return The updated user with the new properties
     * @throws Exception if any problem arise during the update
     */
    @Put("/{id}")
    User updateUser(Long id, @Body Map props) throws Exception {
        userService.updateUser(id, props)
    }

    /**
     * Endpoint for /users/id with POST to save the user
     * @param props properties for the user to save
     * @return The saved user with the new properties
     */
    @Post
    User addUser(@Body Map props) {
        userService.saveUser(props)
    }

    /**
     * Delete endpoint based on the User Id
     * @param id the user to delete based on Id
     */
    @Delete("/{id}")
    HttpResponse deleteUser(Long id) {
        userService.deleteUser(id)

        return HttpResponse.ok()
    }

}

