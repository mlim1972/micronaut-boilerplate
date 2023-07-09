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

docker build --add-host="$MYSQL_CONTAINER_NAME:$HOST_IP" \
--build-arg BOILERPLATE_MYSQL_USER=$BOILERPLATE_MYSQL_USER \
--build-arg BOILERPLATE_MYSQL_PASSWORD=$BOILERPLATE_MYSQL_PASSWORD \
--build-arg BOILERPLATE_MYSQL_URL=$BOILERPLATE_MYSQL_URL \
--rm -f "Dockerfile" \
-t "$IMAGE_NAME":"$version-qa" \
"."

echo "Docker image built: $CONTAINER_NAME"

# remove intermediate builds
docker image rm $(docker images -f "dangling=true" -q)