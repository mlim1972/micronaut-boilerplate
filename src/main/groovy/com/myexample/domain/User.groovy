package com.myexample.domain

import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

@Entity
class User implements GormEntity<User> {
    String email
    String password
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false

    static constraints = {
        email nullable: false, blank: false, email: true
        password nullable: false, blank: false, password: true
    }

    static mapping = {
        password column: '`password`'
    }
}
