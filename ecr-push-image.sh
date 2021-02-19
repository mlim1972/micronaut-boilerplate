#!/bin/bash

#### Need to export these args while calling using this script
#export AWS_ACCESS_KEY_ID={{AWS_ECR_PUSH_ACCESS_KEY}}
#export AWS_SECRET_ACCESS_KEY={{AWS_ECR_PUSH_SECRET_KEY}}
#export AWS_REGION=us-west-2

# Use ECR credentials as auth into docker
# Assumes aws-cli is installed
aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID".dkr.ecr."$AWS_REGION".amazonaws.com/"$IMAGE_NAME":"$IMAGE_VERSION"-qa

# push
docker push "$AWS_ACCOUNT_NUMBER".dkr.ecr."$AWS_REGION".amazonaws.com/"$IMAGE_NAME":"$IMAGE_VERSION"-qa


