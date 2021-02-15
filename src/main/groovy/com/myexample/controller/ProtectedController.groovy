package com.myexample.controller

import com.myexample.domain.User
import groovy.util.logging.Slf4j
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.utils.SecurityService

@Controller("/protected")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Slf4j
class ProtectedController {

    SecurityService securityService

    ProtectedController(SecurityService securityService){
        this.securityService = securityService
    }

    @Get
    Map<String, String> index() {
        def authentication = securityService.authentication.get()
        String email = securityService.username().get()
        String sampleData = authentication.attributes.sampleData

        log.info("email: $email, sampleData: $sampleData")
        def map = [name:"test", email:"test@test.com", age:33]
    }
}
