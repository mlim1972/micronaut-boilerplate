package com.myexample.security

import com.myexample.domain.Role
import com.myexample.domain.User
import com.myexample.service.UserRoleService
import com.myexample.service.UserService
import io.micronaut.http.HttpRequest
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

import io.micronaut.core.annotation.Nullable
import javax.inject.Named
import javax.inject.Singleton
import java.util.concurrent.ExecutorService

@Singleton
class DelegatedUserPasswordAuthProvider implements AuthenticationProvider {

    protected UserService userService
    protected UserRoleService userRoleService
    protected PasswordEncoder passwordEncoder
    protected Scheduler scheduler

    DelegatedUserPasswordAuthProvider(UserService userService,
                                     PasswordEncoder passwordEncoder,
                                     UserRoleService userRoleService,
                                     @Named(TaskExecutors.IO) ExecutorService executorService) {
        this.userService = userService
        this.passwordEncoder = passwordEncoder
        this.userRoleService = userRoleService
        this.scheduler = Schedulers.from(executorService)
    }

    @Override
    Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        Flowable.create({ emitter ->
            User user = userService.findUserByEmail(authenticationRequest.identity)
            Optional<AuthenticationFailed> authenticationFailed = validate(user, authenticationRequest)
            if (authenticationFailed.isPresent()) {
                emitter.onError(new AuthenticationException(authenticationFailed.get()))
            } else {
                emitter.onNext(createSuccessfulAuthenticationResponse(authenticationRequest, user))
            }
            emitter.onComplete()
        }, BackpressureStrategy.ERROR)
                .subscribeOn(scheduler)

    }

    protected Optional<AuthenticationFailed> validate(User user, AuthenticationRequest authenticationRequest) {

        AuthenticationFailed authenticationFailed = null
        if (user == null) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND)

        } else if (!user.isEnabled()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_DISABLED)

        } else if (user.isAccountExpired()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.ACCOUNT_EXPIRED)

        } else if (user.isAccountLocked()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.ACCOUNT_LOCKED)

        } else if (user.isPasswordExpired()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.PASSWORD_EXPIRED)

        } else if (!passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
        }
        Optional.ofNullable(authenticationFailed)
    }

    protected AuthenticationResponse createSuccessfulAuthenticationResponse(AuthenticationRequest authenticationRequest, User user) {
        List<String> roles = userRoleService.findAllAuthoritiesByEmail(user.email)
        new DelegatedUserDetails(user, roles, "SAMPLE DATA")
    }
}