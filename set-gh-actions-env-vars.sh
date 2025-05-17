#!/bin/bash

# NOTE: This should only be called after set-env-vars.sh is called


# Set docker container env vars
echo "IMAGE_NAME=$IMAGE_NAME" >> "$GITHUB_ENV"
echo "IMAGE_VERSION=$IMAGE_VERSION" >> "$GITHUB_ENV"
echo "CONTAINER_NAME=$CONTAINER_NAME" >> "$GITHUB_ENV"

# Create image tags for push to AWS ECR
echo "IMAGE_TAG_LATEST=$IMAGE_TAG_LATEST" >> "$GITHUB_ENV"
echo "IMAGE_TAG_VERSION=$IMAGE_TAG_VERSION" >> "$GITHUB_ENV"

# Set MySQL env vars
echo "HOST_IP=$HOST_IP" >> "$GITHUB_ENV"
echo "MYSQL_CONTAINER_NAME=$MYSQL_CONTAINER_NAME" >> "$GITHUB_ENV"
echo "MYSQL_DBNAME=$MYSQL_DBNAME" >> "$GITHUB_ENV"
echo "MYSQL_URL=$MYSQL_URL" >> "$GITHUB_ENV"
echo "MYSQL_USER=$MYSQL_USER" >> "$GITHUB_ENV"
echo "MYSQL_PASSWORD=$MYSQL_PASSWORD" >> "$GITHUB_ENV"