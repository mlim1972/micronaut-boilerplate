## Micronaut Boilerplate

This is a boilerplate for micronaut using the following features:
- Groovy
- Spock for testing
- MySQL for Database
  - Flyway
  - Tomcat Connection Pool
- Containerize scripts and DB

### 1-MySQLDB branch
- Add DB dependencies in [build.gradle](/build.gradle)
- Using GORM to create entities in the DB
- Added test for Entity controller and service
- Using a MySQL container to use with the project

Before starting, you need to do the following to set the necessary env.
variable for the Database and then run the database via a container.
```
source set-env-vars.sh
./run-mysql.sh
```

### 2-Flyway branch
- Added Flyway
- Updated [application.yml](/src/main/resources/application.yml) to use Flyway
and to disable auto generating tables by setting the `hbm2ddl.auto` to none
- Added [V1__setting_user_table.sql](/src/main/resources/dbmigrations/V1__setting_user_table.sql)
as the first schema creation that flyway will generate

Restart your db by running `./run-mysql.sh` so the db is clean and once you
start your application: `./gradlew run`, you will see a flyway_schema_history
table.

### 3-Flyway_and_Encryption
- Using Flyway Groovy script for things that cannot be done by SQL
- Added a flyway script [V1_1__seed_users](/src/main/groovy/com/myexample/dbmigrationscript/V1_1__seed_users.groovy)
This script seeds a user by using the password encoder to hash the password before
sending to the db.
- Added Spring Security Crypt to get the hashing algorithm to use for password in the build file
- Created classes under the [security](/src/main/groovy/com/myexample/security) folder to
deal with password encoding
- Updated the [UserService](/src/main/groovy/com/myexample/service/UserService.groovy) to
encode the password before it gets to the DB. Since every other code should be using
the service, this is the single point to encode the password 
  
### 4-Docker
This is branch contains shell script to build, run, and create the container for
our application

- [Dockerfile](/Dockerfile). This file setup the necessary conf. to create the image
- [build-image](/build-image.sh). This script will run and create the image file. This
  script can be used during your CI process to build the image
- [ecr-push-image.sh](/ecr-push-image.sh). This is a sample script that shows how to push
  an image to ECR. You will need to do something similar during CI
- [run-image.sh](/run-image.sh). This is script is just to test your image once you build
  it to validate your Docker and build image process. This will not be use in CI but will
  serve as an example when you want to deploy it
- [set-env-vars.sh](/set-env-vars.sh). This file was modified to reflect env. variables
  for AWS access. The most important is your AWS Account number so, that it can tag your
  image appropriately.
  
### 5-JWT
This branch show how to use the Micronaut JWT security feature.  We need to create
roles and provide different delegates to provide the authentication needed. We are using
JWT token to provide login authentication. The code also includes a custom header as part
of the authentication token. Here are the list of changes:
- Updated all Controllers to add the `@Secured` annotation to Anonymous or Authenticated
- Protected controller is an example of a controller needed authentication to access it.
It also shows how to get the current logon user and whatever extra information from the JWT
token
- Added Delegates to the [security](/src/main/groovy/com/myexample/security) folder
- Updated the [application.yml](/src/main/resources/application.yml) file to user 
JWT as authentication
- Added a new dbmigration to add roles and to the DB
- Added role related domain objects and services
- Updated the [build.gradle](/build.gradle) to include the security libraries for JWT