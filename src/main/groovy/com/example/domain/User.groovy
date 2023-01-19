package com.example.domain

import groovy.transform.ToString
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated

import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Version
import javax.persistence.Column
import javax.validation.constraints.Email
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * Entity class for User. This class is the representation of the User
 * table.
 */
@Entity
@Table(name = "user")
@ToString
class User implements Serializable {
    private static final long serialVersionUID = 1L
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id  // this is the PK identified by Id and it's autogenerated

    @Version
    Long version = 0L

    String firstName // can be null or empty

    @NotNull
    @NotEmpty
    String lastName  // cannot be null or empty

    @Column(unique=true)
    @Email(regexp = ".+[@].+[\\.].+")
    String username  // email type and unique constraint created

    // security fields
    @NotNull
    @NotEmpty
    String password
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false

    // eager fetching roles
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    Set<UserRole> roles

    @DateCreated
    Date dateCreated    // managed by the framework when the records is added

    @DateUpdated
    Date dateUpdated    // managed by the framework when the record is updated

    @Column(columnDefinition = "Text")
    String notes    // text field... Longer than varchar(4000)
}
