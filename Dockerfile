# This is a multi-stage build. The first stage is used to build the application
# and the second stage is used to run the application.
FROM gradle:8.14.0-jdk21 AS build-env

# Set build arguments from build-image.sh
ARG MYSQL_URL
ARG MYSQL_USER
ARG MYSQL_PASSWORD

ARG BOILERPLATE_PORT
ARG BOILERPLATE_JAVA_OPTS

# Set the working directory to /home
WORKDIR /home

# Copy the gradle files over.
COPY --chown=gradle:gradle . ./

# Compile the application w/o running tests since the tests use Testcontainers
RUN gradle build --stacktrace --no-daemon -x test


### Run stage ###
# Other images to consider:
#FROM openjdk:11.0.16-jre-slim-buster    # 318MB
#FROM openjdk:17.0.2-jdk-slim            # 511MB
#FROM eclipse-temurin:17.0.7_7-jre-focal # 389MB

# Distroless is more secured and smaller than the above images
# The total size is around 273MB
FROM gcr.io/distroless/java21-debian12

# create a user and group to run the app
# not needed when using distroless
#RUN addgroup --system demogroup && adduser --system demouser --ingroup demogroup
#RUN addgroup -S demogroup && adduser -S demouser -G demogroup

# Set the working directory to /home
#WORKDIR /home

# Copy the compiled files over.
COPY --from=build-env /home/build/libs/boilerplate-*-all.jar boilerplate.jar

# Change the owner of the home directory to the new user
# This is a security best practice. We should not run the app as root.
# When using distroless, there is no need to do this.
#RUN chown -R demouser:demogroup /home
# Change to the new user
#USER demouser:demogroup

# Distroless uses nonroot as the non-priviliged user
USER nonroot

# Expose the port
EXPOSE ${BOILERPLATE_PORT}

# Starts the java app
# if not using distroless, use this:
#CMD java ${BOILERPLATE_JAVA_OPTS} -jar boilerplate.jar $0 $@

# Distrolesss already have Java installed, so we don't need to specify the java command
# The command to execute starts with the jar file!
ENV JDK_JAVA_OPTIONS="$BOILERPLATE_JAVA_OPTS"
CMD ["-jar", "boilerplate.jar"]

