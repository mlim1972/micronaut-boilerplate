
FROM gradle:6.8.2-jre11 AS build-env

# Set build arguments from build-image.sh
ARG MYSQL_URL
ARG MYSQL_USER
ARG MYSQL_PASSWORD

# Set the working directory to /home
WORKDIR /home

COPY --chown=gradle:gradle . ./

# Compile the application.
RUN gradle build --no-daemon

FROM openjdk:11.0.10-jre-slim-buster
# Using distroless
#FROM gcr.io/distroless/java:11

# Set the working directory to /home
WORKDIR /home

# Copy the compiled files over.
COPY --from=build-env /home/build/libs/demo-*-all.jar /home/demo.jar

# Expose the port
EXPOSE 8080

# Starts the java app
# Using EntryPoing to pass env. variables as describe in this article:
ENV MY_JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=80.0 -noverify -XX:+AlwaysPreTouch"
ENTRYPOINT exec java $JAVA_OPTS $MY_JAVA_OPTS -jar demo.jar $0 $@
