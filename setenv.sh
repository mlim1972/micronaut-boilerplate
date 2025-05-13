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

export BOILERPLATE_PORT=8080

# Set MySQL env vars
export HOST_IP=$hostIp
export MYSQL_DBNAME=demo

export MYSQL_PORT=3306
export MYSQL_URL="jdbc:mysql://$HOST_IP:$MYSQL_PORT/$MYSQL_DBNAME?allowPublicKeyRetrieval=true&useSSL=true"
export MYSQL_USER=demouser
export MYSQL_PASSWORD=demopasswd

export JWT_GENERATOR_SIGNATURE_SECRET="ns#tzdgf8&^&;PiojTDtwibm49q34rqsg-^/erh5"
