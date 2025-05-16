package com.example.domain

import com.example.jsonview.ApiView
import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
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
@EqualsAndHashCode
@JsonView(ApiView.Public.class)
class Role implements Serializable{
    private static final long serialVersionUID = 1L

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Version
    @JsonView(ApiView.Internal.class)
    Long version = 0L

    @NotNull
    @NotEmpty
    String authority
}
