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