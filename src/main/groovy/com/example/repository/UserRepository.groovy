package com.example.repositories

import com.example.domains.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.CrudRepository

/**
 * Magic interface! Micronaut will generate an abstract instance of this
 * interface upon starting
 */
@Repository
interface UserRepository extends CrudRepository<User, Long>{
    Page<User> list(Pageable pageable)
}