package com.example.service.security

import io.micronaut.security.authentication.AuthenticationException

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

/**
 * An AuthenticationProvider is responsible for taking an AuthenticationRequest and determining if it can
 * authenticate it. If it can then it should return an AuthenticationResponse that is successful.
 * If it cannot then it should return an AuthenticationResponse that is unsuccessful.
 */
@Singleton
class AuthProviderService implements AuthenticationProvider {
    UserService userService
    Scheduler scheduler
    PasswordEncoder passwordEncoder

    /**
     * Constructor for the AuthProviderService. The constructor will be called by Micronaut and the
     * UserService and PasswordEncoder will be injected
     * @param userService The UserService to find the user
     * @param passwordEncoder The PasswordEncoder to check the password validity
     * @param executorService The ExecutorService to run the blocking code on
     */
    AuthProviderService(UserService userService, PasswordEncoder passwordEncoder,
                        @Named(TaskExecutors.IO) ExecutorService executorService) {
        this.userService = userService
        this.passwordEncoder = passwordEncoder

        this.scheduler = Schedulers.fromExecutorService(executorService)
    }

    /**
     * This method is called by authenticate to validate the user state in the system. If the user is valid
     * then it return null otherwise it returns an AuthenticationFailed with the reason why the user is not
     * valid
     * @param user The user to check the validity of the password
     * @param authenticationRequest The authentication request containing the username and password
     * @return null if the user is valid otherwise an AuthenticationFailed with the reason why the user is not
     */
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

    /**
     * This method is called from authenticate to create the successful authentication response
     * @param user The user to create the AuthenticationResponse for
     * @return The AuthenticationResponse with the username and roles
     */
    private AuthenticationResponse createSuccessfulAuthenticationResponse(User user) {
        Set<String> authorities = [] as Set<String>
        if(user.roles) user.roles.collect { it.authority } as Set<String>
        // if more attributes are needed, they can be added here.
        // The third parameter is a map of attributes...
        AuthenticationResponse.success(user.username, authorities)
    }

    /**
     * This method is called by Micronaut to determine if the user is a valid user in the system
     * @param httpRequest The http request
     * @param authenticationRequest The credentials to authenticate
     * @return A Publisher of AuthenticationResponse that is successful if the user is valid or unsuccessful if the user is not valid
     */
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
}
