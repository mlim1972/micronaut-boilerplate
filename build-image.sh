#!/bin/bash

hostIp=$(hostname -I | awk -v OFS=' ' '{print $1}')
#hostIp=127.0.0.1

# get version from build.gradle
version=$(grep "^version" ./build.gradle | awk -v OFS=' ' '{print $3}' | sed 's/"//g')
# Using static value because of issue #13035
#version=1.0

# Remove previous instance of container
# If the container exists
if [ "$(docker ps -q -f status=running -f name="$CONTAINER_NAME")" ]; then
  # If the container is running
  echo "Stopping container $CONTAINER_NAME"
  docker stop "$CONTAINER_NAME"
fi

if [ "$(docker ps -a -q -f name="$CONTAINER_NAME")" ]; then
  echo "Removing container $CONTAINER_NAME"
  docker rm "$CONTAINER_NAME"
fi

# do image build
# All environment variables are set in set-env-vars.sh or Github actions
docker build --add-host="$MYSQL_CONTAINER_NAME":"$hostIp" \
  --build-arg MYSQL_URL="$MYSQL_URL" \
  --build-arg MYSQL_USER="$MYSQL_USER" \
  --build-arg MYSQL_PASSWORD="$MYSQL_PASSWORD" \
  --rm -f "Dockerfile" \
  -t "$IMAGE_NAME":"$version-qa" \
  -t "$AWS_ACCOUNT_NUMBER".dkr.ecr.us-west-2.amazonaws.com/"$IMAGE_NAME":"$version-qa" \
  "."

echo "Docker image built: $CONTAINER_NAME"


# remove intermediate builds
docker image rm $(docker images -f "dangling=true" -q)
