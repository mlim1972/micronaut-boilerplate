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
echo "Creating schema in DB with details. DB: $MYSQL_DBNAME, user: $MYSQL_USER, pass: $MYSQL_PASSWORD"
docker exec -i "$MYSQL_CONTAINER_NAME" \
mysql -uroot -proot \
-e "SET @MYSQL_DBNAME='$MYSQL_DBNAME';
SET @MYSQL_USER='$MYSQL_USER';
SET @MYSQL_PASSWORD='$MYSQL_PASSWORD';
source setup-schema.sql;"