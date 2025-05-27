package com.example

import io.micronaut.context.env.PropertySource
import io.micronaut.core.annotation.NonNull
import io.micronaut.management.endpoint.info.InfoSource
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

/**
 * ExampleInfoSource is a custom InfoSource that provides additional information
 * to the Micronaut Info endpoint.
 */
@Singleton
class ExampleInfoSource implements InfoSource {

    /**
     * Overrides the getSource method to provide custom information.
     * This method returns a Publisher that emits a PropertySource with the custom information.
     */
    @Override
    @NonNull
    Publisher<PropertySource> getSource() {
        Map<String, String> exampleInfo = Collections.singletonMap("message", "Hello from ExampleInfoSource!")
        return Flux.just(PropertySource.of("example", exampleInfo))
    }
}