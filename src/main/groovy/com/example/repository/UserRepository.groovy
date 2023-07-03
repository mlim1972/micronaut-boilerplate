package com.example.repository

import com.example.domain.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Slice
import io.micronaut.data.repository.PageableRepository

/**
 * Magic interface! Micronaut will generate an abstract instance of this
 * interface upon starting
 */
@Repository
interface UserRepository extends PageableRepository<User, Long> {
    Slice<User> list(Pageable pageable)
    User findOneByUsername(String username)
}