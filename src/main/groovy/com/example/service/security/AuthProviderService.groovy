package com.example.service.security

import io.micronaut.security.authentication.AuthenticationException
import reactor.core.publisher.FluxSink

import com.example.domain.User
import com.example.service.UserService
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.http.HttpRequest
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

import java.util.concurrent.ExecutorService

import static io.micronaut.security.authentication.AuthenticationFailureReason.ACCOUNT_EXPIRED
import static io.micronaut.security.authentication.AuthenticationFailureReason.ACCOUNT_LOCKED
import static io.micronaut.security.authentication.AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH
import static io.micronaut.security.authentication.AuthenticationFailureReason.PASSWORD_EXPIRED
import static io.micronaut.security.authentication.AuthenticationFailureReason.USER_DISABLED
import static io.micronaut.security.authentication.AuthenticationFailureReason.USER_NOT_FOUND

@Singleton
class AuthProviderService implements AuthenticationProvider {
    UserService userService
    Scheduler scheduler
    PasswordEncoder passwordEncoder

    AuthProviderService(UserService userService, PasswordEncoder passwordEncoder,
                        @Named(TaskExecutors.IO) ExecutorService executorService) {
        this.userService = userService
        this.passwordEncoder = passwordEncoder

        this.scheduler = Schedulers.fromExecutorService(executorService)
    }

    private AuthenticationFailed validate(User user, AuthenticationRequest authenticationRequest) {
        AuthenticationFailed authenticationFailed = null
        if (!user) {
            authenticationFailed = new AuthenticationFailed(USER_NOT_FOUND)

        } else if (!user.enabled) {
            authenticationFailed = new AuthenticationFailed(USER_DISABLED)

        } else if (user.accountExpired) {
            authenticationFailed = new AuthenticationFailed(ACCOUNT_EXPIRED)

        } else if (user.accountLocked) {
            authenticationFailed = new AuthenticationFailed(ACCOUNT_LOCKED)

        } else if (user.passwordExpired) {
            authenticationFailed = new AuthenticationFailed(PASSWORD_EXPIRED)

        } else if (!passwordEncoder.matches(authenticationRequest.secret.toString(), user.password)) {
            authenticationFailed = new AuthenticationFailed(CREDENTIALS_DO_NOT_MATCH)
        }

        authenticationFailed
    }

    private AuthenticationResponse createSuccessfulAuthenticationResponse(User user) {
        Set<String> authorities = user.roles.collect { it.role.authority } as Set<String>
        AuthenticationResponse.success(user.username, authorities)
    }

    @Override
    Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {

        Flux.create { emitter ->
            User user = userService.findByUsername(authenticationRequest.identity.toString())
            AuthenticationFailed authenticationFailed = validate(user, authenticationRequest)
            if (authenticationFailed) {
                emitter.error(new AuthenticationException(authenticationFailed))
            } else {
                emitter.next(createSuccessfulAuthenticationResponse(user))
                emitter.complete()
            }
        }.subscribeOn(scheduler)
    }

    // Hardcoded validation for a user with username "sherlock" and password "password"
//    Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
//                                                   AuthenticationRequest<?, ?> authenticationRequest) {
//        Flux.create(emitter -> {
//            if (authenticationRequest.identity == "sherlock" && authenticationRequest.secret == "password") {
//                emitter.next(AuthenticationResponse.success((String) authenticationRequest.identity))
//                emitter.complete()
//            } else {
//                emitter.error(AuthenticationResponse.exception())
//            }
//        }, FluxSink.OverflowStrategy.ERROR)
//    }
}
