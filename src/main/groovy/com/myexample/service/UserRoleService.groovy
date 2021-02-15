package com.myexample.service

import com.myexample.domain.Role
import com.myexample.domain.User
import com.myexample.service.gorm.UserRoleGormService
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Singleton

@Singleton
@CompileStatic
@Transactional
@Slf4j
class UserRoleService {
    UserRoleGormService userRoleGormService

    UserRoleService(UserRoleGormService userRoleGormService){
        this.userRoleGormService = userRoleGormService
    }

    List<String> findAllAuthoritiesByEmail(String email){
        userRoleGormService.findAllAuthoritiesByEmail(email)
    }
}
