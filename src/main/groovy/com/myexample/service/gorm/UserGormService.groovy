package com.myexample.service.gorm

import com.myexample.domain.User
import grails.gorm.services.Service

@Service(User)
interface UserGormService {

    User save(String email, String password)

    User findByEmail(String email)

    User findById(Serializable id)

    List<User> findAll()

    void delete(Serializable id)

    int count()
}