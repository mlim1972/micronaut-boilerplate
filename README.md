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

