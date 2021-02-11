package com.myexample.service

import com.myexample.domain.User
import com.myexample.security.PasswordEncoder
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
    protected final UserGormService userGormService
    protected final PasswordEncoder passwordEncoder

    UserService(UserGormService userGormService, PasswordEncoder passwordEncoder){
        this.userGormService = userGormService
        this.passwordEncoder = passwordEncoder
    }

    User findUserByEmail(String email){
        userGormService.findByEmail(email)
    }

    List<User> findAll(){
        userGormService.findAll()
    }

    User saveUser(String email, String password){
        final String encodedPassword = passwordEncoder.encode(password)
        userGormService.save(email, encodedPassword)
    }
}
