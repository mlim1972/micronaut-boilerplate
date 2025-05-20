package com.example.controller

import com.example.domain.User
import groovy.util.logging.Slf4j
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.utils.SecurityService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/protected")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Slf4j
class ProtectedController {

    SecurityService securityService

    ProtectedController(SecurityService securityService){
        this.securityService = securityService
    }

    @SecurityRequirement(name = "BearerAuth")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Greets generic info",
            description = "A sample protected API"
    )
    @ApiResponse(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type="User"))
    )
    @ApiResponse(responseCode = "400", description = "Invalid Name Supplied")
    @ApiResponse(responseCode = "404", description = "Data not found")
    @Tag(name = "protected")
    @Get
    Map<String, String> index() {
        def authentication = securityService.authentication.get()
        String email = securityService.username().get()
        String sampleData = authentication.attributes.sampleData

        log.info("email: $email, sampleData: $sampleData")
        def map = [name:"test", email:"test@test.com", age:33]
    }
}
