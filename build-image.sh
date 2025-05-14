#!/bin/bash

source setenv.sh

# get version from build.gradle
version=$(grep "^version" ./build.gradle | awk -v OFS=' ' '{print $3}' | sed 's/"//g')

# Remove previous instance of container
# If the container exists
if [ "$(docker ps -q -f status=running -f name="$CONTAINER_NAME")" ]; then
  # If the container is running
  echo "Stopping container $CONTAINER_NAME"
  docker stop "$CONTAINER_NAME"
fi

if [ "$(docker ps -a -q -f name="$CONTAINER_NAME")" ]; then
  echo "Removing container $CONTAINER_NAME"
  docker rm -f "$CONTAINER_NAME"
fi

# 1. First run tests with local Docker since the tests
# use Testcontainers and we cannot do docker in docker
# (dind) in the Dockerfile.
./gradlew test

# 2. Only if tests pass, build the image... The MYSQL arguments
# are not necessary for now because the Dockerfile does not
# perform any tests since we are performing the test above.
docker build --add-host="$MYSQL_CONTAINER_NAME:$HOST_IP" \
--build-arg MYSQL_USER=$MYSQL_USER \
--build-arg MYSQL_PASSWORD=$MYSQL_PASSWORD \
--build-arg MYSQL_URL=$MYSQL_URL \
--rm -f "Dockerfile" \
-t "$IMAGE_NAME":"$version-qa" \
"."

echo "Docker image built: $CONTAINER_NAME"

# remove intermediate builds
docker image rm $(docker images -f "dangling=true" -q)