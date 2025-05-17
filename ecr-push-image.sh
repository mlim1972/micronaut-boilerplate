#!/bin/bash

# Use ECR credentials as auth into docker
# aws --profile xdp-push-usr ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 390693405880.dkr.ecr.us-west-2.amazonaws.com/$IMAGE_NAME:$version-qa
# Assumes aws-cli is installed
aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID".dkr.ecr."$AWS_REGION".amazonaws.com/"$IMAGE_NAME":"$IMAGE_VERSION"-qa

# push
# $AWS_ACCOUNT_NUMBER represents your account number
docker push "$AWS_ACCOUNT_NUMBER".dkr.ecr."$AWS_REGION".amazonaws.com/"$IMAGE_NAME":"$IMAGE_VERSION"-qa
