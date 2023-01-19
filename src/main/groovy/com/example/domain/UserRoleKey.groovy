package com.example.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class UserRoleKey implements Serializable {
    @Column(name = "user_id")
    Long userId

    @Column(name = "role_id")
    Long roleId
}
