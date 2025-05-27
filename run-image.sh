#!/bin/bash

source setenv.sh

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


docker run -m 2GB --detach --name "$CONTAINER_NAME" --hostname "$HOST_NAME" \
--add-host="$MYSQL_CONTAINER_NAME:$HOST_IP" \
--env="BOILERPLATE_PORT=$BOILERPLATE_PORT" \
--env="MYSQL_URL=$MYSQL_URL" \
--env="MYSQL_USER=$MYSQL_USER" \
--env="MYSQL_PASSWORD=$MYSQL_PASSWORD" \
--env="JWT_GENERATOR_SIGNATURE_SECRET=$JWT_GENERATOR_SIGNATURE_SECRET" \
-p $BOILERPLATE_PORT:$BOILERPLATE_PORT \
-p 9103:9103 \
"$IMAGE_NAME":"$version-qa"

echo "Started container $CONTAINER_NAME"