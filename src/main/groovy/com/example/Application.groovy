package com.example

import io.micronaut.openapi.annotation.OpenAPIInclude
import io.micronaut.runtime.Micronaut
import groovy.transform.CompileStatic
import io.micronaut.security.endpoints.LoginController
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.tags.Tag

@OpenAPIDefinition(
        info = @Info(
                title = "Micronaut Boilerplate",
                version = "0.1",
                description = "Example of a microservice implemented using Micronaut",
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(url = "https://github.com/mlim1972/micronaut-boilerplate", name = "Mario", email = "mlim1972@gmail.com")
        )
)
@OpenAPIInclude(
        classes = [ LoginController.class ],
        tags = @Tag(name = "Security")
)
//swagger config to set globally the jwt authentication header on the api doc page
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "jwt"
)
@CompileStatic
class Application {

    static void main(String[] args) {
        Micronaut.run(Application, args)
    }
}
