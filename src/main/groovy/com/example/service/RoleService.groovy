package com.example.service

import com.example.domain.Role
import com.example.repository.RoleRepository
import groovy.util.logging.Slf4j
import io.micronaut.transaction.annotation.ReadOnly
import jakarta.inject.Singleton

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@Singleton
@Slf4j
class RoleService {
    RoleRepository roleRepository
    EntityManager entityManager

    RoleService(RoleRepository roleRepository, EntityManager entityManager){
        this.roleRepository = roleRepository
        this.entityManager = entityManager
    }

    /**
     * This method saves the role to the role table
     * @param role is the role to be saved
     * @return the saved role
     */
    @Transactional
    Role save(Role role){
        roleRepository.save(role)
    }

    /**
     * This method adds a new role to the role table
     * @param roleName is the name of the role
     * @return the newly created role
     */
    @Transactional
    Role addRole(String roleName) {
        def role = new Role(authority: roleName)
        roleRepository.save(role)
    }

    /**
     * This method adds a list of role names to the role table
     * @param roleNames is the list of role names
     * @return the list of newly created roles
     */
    @Transactional
    List<Role> addRoles(ArrayList<String>roleNames) {
        def roles = roleNames.collect { new Role(authority: it) }
        return roleRepository.saveAll(roles).collect{it}
    }

    /**
     * This method gets the role by name
     * @param roleName is the name of the role
     * @return the role from the DB. Otherwise, it returns null
     */
    @ReadOnly
    Role getRoleByName(String roleName){
        roleRepository.findByAuthority(roleName).orElse(null)
    }

    /**
     * This method gets the role by id
     * @param id is the id of the role
     * @return
     */
    @ReadOnly
    Role getRoleById(Long id){
        roleRepository.findById(id).orElse(null)
    }
}
