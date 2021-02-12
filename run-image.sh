#!/bin/bash

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
  docker rm "$CONTAINER_NAME"
fi

# All environment variables are set in set-env-vars.sh or Github actions
# Increase memory by means of Docker -m
# -m 3GB \
# Any other Java parameters can be entered as follows: -e JAVA_OPTS='-Xmx3g -Xms3g' \
docker run --detach --name "$CONTAINER_NAME" \
--env="MYSQL_URL=$MYSQL_URL" \
--env="MYSQL_USER=$MYSQL_USER" \
--env="MYSQL_PASSWORD=$MYSQL_PASSWORD" \
-m 512MB \
-p 8080:8080 "$IMAGE_NAME":"$version-qa"

echo "Started container $CONTAINER_NAME"
