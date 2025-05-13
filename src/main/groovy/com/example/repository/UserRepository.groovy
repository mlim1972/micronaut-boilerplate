package com.example.repository

import com.example.domain.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.repository.CrudRepository

/**
 * Magic interface! Micronaut will generate an abstract instance of this
 * interface upon starting
 */
@Repository
interface UserRepository extends JpaRepository<User, Long>{
//interface UserRepository extends CrudRepository<User, Long>{
    Page<User> list(Pageable pageable)

    User findOneByUsername(String username)
}