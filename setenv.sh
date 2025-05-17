#!/bin/bash

# Set host inet ip
if [[ "$OSTYPE" == "darwin"* ]]; then
  hostIp=$(ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}')
else
  hostIp=$(hostname -I | awk -v OFS=' ' '{print $1}')
fi

# get version from build.gradle
version=$(grep "^version" ./build.gradle | awk -v OFS=' ' '{print $3}' | sed 's/"//g')
#version=1.0

# AWS info
if [ -z "${AWS_ACCOUNT_NUMBER}" ]; then
  export AWS_ACCOUNT_NUMBER=xxxxxxxxxx
else
  echo "‘AWS_ACCOUNT_NUMBER’ variable is set to: $AWS_ACCOUNT_NUMBER"
fi

if [ -z "${AWS_REGION}" ]; then
  export AWS_REGION=us-west-2
else
  echo "‘AWS_REGION’ variable is set to: AWS_DEFAULT_REGION"
fi

export IMAGE_VERSION=$version

# Set docker container env vars
export CONTAINER_NAME=demo-instance
export IMAGE_NAME=my-demo
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
