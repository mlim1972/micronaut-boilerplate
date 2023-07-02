package com.example.domain

import groovy.transform.EqualsAndHashCode

import javax.persistence.Column
import javax.persistence.Embeddable

@EqualsAndHashCode
@Embeddable
class UserRoleKey implements Serializable {
    @Column(name = "user_id")
    Long userId

    @Column(name = "role_id")
    Long roleId
}
