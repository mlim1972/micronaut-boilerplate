package com.example.domain

import groovy.transform.ToString
import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Version
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

@Entity
@ToString
@Serdeable
class Role implements Serializable{
    private static final long serialVersionUID = 1L

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Version
    Long version = 0L

    @NotNull
    @NotEmpty
    String authority
}
