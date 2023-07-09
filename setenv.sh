#!/bin/bash

# Set host inet ip
if [[ "$OSTYPE" == "darwin"* ]]; then
  echo "Running Mac"
  hostIp=$(ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}' | awk 'END{print}')
elif [[ "$OSTYPE" == "linux"* ]]; then
  echo "Running Linux"
  hostIp=$(hostname -I | awk -v OFS=' ' '{print $1}')
else
  echo "Unknown OS"
  exit 1
fi

# set host ip and name
export HOST_IP=$hostIp
export HOST_NAME=boilerplate

# AWS info
export AWS_ACCOUNT_NUMBER=xxxxxxxxxx
export AWS_DEFAULT_REGION=us-west-2

# Container and image name
export CONTAINER_NAME=micronaut-boilerplate
export IMAGE_NAME=micronaut-boilerplate-image

# MySQL Container name running on host
export MYSQL_CONTAINER_NAME=my-mysql

# Application PORT and JAVA_OPTS
export BOILERPLATE_PORT=8080
export BOILERPLATE_JAVA_OPTS="-Xmx1g -Xms1g"

# DB specific env vars
export BOILERPLATE_MYSQL_DBNAME=demo
export BOILERPLATE_MYSQL_URL="jdbc:mysql://$MYSQL_CONTAINER_NAME:3306/$BOILERPLATE_MYSQL_DBNAME?allowPublicKeyRetrieval=true&useSSL=true"
export BOILERPLATE_MYSQL_USER=demouser
export BOILERPLATE_MYSQL_PASSWORD=demopasswd

# JWT specific env vars
export JWT_GENERATOR_SIGNATURE_SECRET="ns#tzdgf8&^&;PiojTDtwibm49q34rqsg-^/erh5"
