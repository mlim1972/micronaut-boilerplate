#!/bin/bash

# Set host inet ip
if [[ "$OSTYPE" == "darwin"* ]]; then
  hostIp=$(ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}')
else
  hostIp=$(hostname -I | awk -v OFS=' ' '{print $1}')
fi

# get version from build.gradle
version=$(grep "^version" ./build.gradle | awk -v OFS=' ' '{print $3}' | sed 's/"//g')

# Set docker container env vars
export MYSQL_CONTAINER_NAME=my-mysql

# Set MySQL env vars
export HOST_IP=$hostIp
export MYSQL_DBNAME=demo
#export MYSQL_URL="jdbc:mysql://$HOST_IP:3306/$MYSQL_DBNAME?allowPublicKeyRetrieval=true&useSSL=false"
export MYSQL_USER=demouser
export MYSQL_PASSWORD=demopasswd