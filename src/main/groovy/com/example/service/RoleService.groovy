package com.example.service

import com.example.domain.Role
import com.example.repository.RoleRepository
import groovy.util.logging.Slf4j
import jakarta.inject.Singleton

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@Singleton
@Transactional
@Slf4j
class RoleService {
    RoleRepository roleRepository
    EntityManager entityManager

    RoleService(RoleRepository roleRepository, EntityManager entityManager){
        this.roleRepository = roleRepository
        this.entityManager = entityManager
    }

    Role save(Role role){
        roleRepository.save(role)
    }

    Role addRole(String roleName) {
        def role = new Role(authority: roleName)
        roleRepository.save(role)
    }

    List<Role> addRoles(ArrayList<Role>roleNames) {
        def roles = roleNames.collect { new Role(authority: it) }
        return roleRepository.saveAll(roles).collect{it}
    }
}
