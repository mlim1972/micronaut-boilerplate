package com.example.domain

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId

@Entity
class UserRole implements Serializable{
    @EmbeddedId
    UserRoleKey id

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    Role role
}
