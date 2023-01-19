package com.example.repository

import com.example.domain.Role
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface RoleRepository extends CrudRepository<Role, Long> {
}
