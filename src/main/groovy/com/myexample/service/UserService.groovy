package com.myexample.service

import com.myexample.domain.User
import com.myexample.service.gorm.UserGormService
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Singleton

@Singleton
@CompileStatic
@Transactional
@Slf4j
class UserService {
    UserGormService userGormService

    UserService(UserGormService userGormService){
        this.userGormService = userGormService
    }

    User findUserByEmail(String email){
        userGormService.findByEmail(email)
    }

    List<User> findAll(){
        userGormService.findAll()
    }

    User saveUser(String email, String password){
        userGormService.save(email, password)
    }
}
