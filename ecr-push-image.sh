#!/bin/bash

# aws cli should be installed
# setup users and policies to ECR : http://blog.shippable.com/setting-permissions-on-aws-ec2-ecr-repositories

#### Need to export these args while calling using this script
#export AWS_REGISTRY={{AWS_REGISTRY}}
#export AWS_ACCESS_KEY_ID={{AWS_ECR_PUSH_ACCESS_KEY}}
#export AWS_SECRET_ACCESS_KEY={{AWS_ECR_PUSH_SECRET_KEY}}
#export AWS_DEFAULT_REGION=us-west-2

version=$(grep "^version" ./build.gradle | awk -v OFS=' ' '{print $3}' | sed 's/"//g')

# create repository if needed
#aws --profile admin ecr create-repository --repository-name=demo/boilerplate --region ${AWS_DEFAULT_REGION}

# Use ECR credentials as auth into docker
# aws --profile xdp-push-usr ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 390693405880.dkr.ecr.us-west-2.amazonaws.com/$IMAGE_NAME:$version-qa
aws ecr get-login-password --region $AWS_DEFAULT_REGION | docker login --username AWS --password-stdin "$AWS_ACCOUNT_NUMBER".dkr.ecr."$AWS_DEFAULT_REGION".amazonaws.com/$IMAGE_NAME:$version-qa

# push
# $AWS_ACCOUNT_NUMBER represents your account number
docker push "$AWS_ACCOUNT_NUMBER".dkr.ecr."$AWS_DEFAULT_REGION".amazonaws.com/$IMAGE_NAME:$version-qa

