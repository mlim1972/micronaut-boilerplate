#!/bin/bash

source setenv.sh

# remove previous instance of my sql
# If the container exists
if [ "$(docker ps -q -f status=running -f name="$MYSQL_CONTAINER_NAME")" ]; then
  # If the container is running, stop it
  echo "Stopping container $MYSQL_CONTAINER_NAME"
  docker stop "$MYSQL_CONTAINER_NAME"
fi

# Now, remove the container
if [ "$(docker ps -a -q -f name="$MYSQL_CONTAINER_NAME")" ]; then
  echo "Removing container $MYSQL_CONTAINER_NAME"
  docker rm "$MYSQL_CONTAINER_NAME"
fi

#get mysql server running in port 6603
docker run -d --name="$MYSQL_CONTAINER_NAME" --env="MYSQL_ROOT_PASSWORD=root" -p "$MYSQL_PORT":3306 mysql:8.0.42

#wait for mysql server to come up
echo "Waiting for MySQL container to come up."
while true; do
  echo -n "."
  docker exec -i "$MYSQL_CONTAINER_NAME" mysql -h "$HOST_IP" -P 3306 -uroot -proot -e "quit" > /dev/null 2>&1
  if [ "$?" -eq 0 ]; then
    echo ""
    echo "Connected to MySQL!"
    break
  else
    sleep 1
  fi
done

## Copy the schema to the container
echo ""
echo "Copying schema setup-schema.sql to container: $MYSQL_CONTAINER_NAME"
docker cp setup-schema.sql "$MYSQL_CONTAINER_NAME":/setup-schema.sql

#executing the schema setup
echo "Creating schema in DB with details. DB: $BOILERPLATE_MYSQL_DBNAME, user: $BOILERPLATE_MYSQL_USER, pass: $BOILERPLATE_MYSQL_PASSWORD"
docker exec -i "$MYSQL_CONTAINER_NAME" \
mysql -uroot -proot \
-e "SET @BOILERPLATE_MYSQL_DBNAME='$BOILERPLATE_MYSQL_DBNAME';
SET @BOILERPLATE_MYSQL_USER='$BOILERPLATE_MYSQL_USER';
SET @BOILERPLATE_MYSQL_PASSWORD='$BOILERPLATE_MYSQL_PASSWORD';
source setup-schema.sql;"