## Micronaut Boilerplate

This repo is a boilerplate for [Micronaut](https://micronaut.io/). The project was created using 
the [launch button](https://micronaut.io/launch/) in their website. The initial project uses
the following configuration:
- Type: Micronaut Application
  - Micronaut Version: 4.8.2
- Java Version: 21
  - Language: Groovy
- Name: boilerplate
- Build Tool: Gradle
- Base Package: com.example
- Test Framework: Spock

and the following Features:
- data-jpa
- mysql
- flyway
- jdbc-tomcat
- netty-server

<p align="center">
<img alt="Launch Configuration" src="images/Micronaut_Launch.png">
<b><sub>Fig.1 - Launch Configuration</sub></b>
</p>

This boilerplate will be a guide to many features microservices use like database
connectivity, containers, authentication, OpenAPI, tracing, etc... 
Those features will be added as branches that build on top of the 
previous one.
Comparing branches can be done by initiating the creation of a Pull Request and 
comparing the branches.

<p align="center">
<img alt="Pull Request branch comparison" src="images/github-compare.png">
<b><sub>Fig.2 - Pull Request branch comparison</sub></b>
</p>

## Branches
### main
The main branch is the initial project configured as mentioned above and following the 
[CREATING YOUR FIRST MICRONAUT APPLICATION](https://guides.micronaut.io/latest/creating-your-first-micronaut-app-gradle-groovy.html)
guide with a few changes:
- Used Launch Button from the website rather than using the CLI
- [HelloController](src/main/groovy/com/example/controller/HelloController.groovy) is in a folder called controllers, so we can add all our controllers here
- Added a service class [HelloService](src/main/groovy/com/example/service/HelloService.groovy) under the services folder to store all our services
- Created [controllers](src/test/groovy/com/example/controller) and 
  [services](src/test/groovy/com/example/service) test files 
  for their corresponding classes

### 1.ORM
This branch adds a good amount of changes. The main addition is the usage of ORM to manage
database actions.
Here is the list of changes:
- [build.gradle](build.gradle). New entries are added to the build file:
  - Micronaut Data Processor. This will work in conjunction to the Micronaut Data and JPA
  - Spring Security Crypto. This library brings the Spring Security Crypto library for password encoding
  - slf4j compatibility layer for logging
  - H2 DB for runtime testing only
- [controller/UserController](src/main/groovy/com/example/controller/UserController.groovy).
  A new controller for managing the user endpoints.
  - **@ExecuteOn** annotations that specifies which executor the task should run on 
  - **@Controller** annotation marks this class as a controller
  - **@Slf4j** annotation to get a hold of the logger
  - **@Nullable** annotation specifying that the field could be null
  - **@QueryValue** annotation tells the method that this argument comes from the query param
- [domain/User](src/main/groovy/com/example/domain/User.groovy). This is the domain
  object for User. The domain object will be use as the user table represented
  as an object. This class uses the following annotations:
  - **@Table** annotation to change the name of the database table
  - **@Entity** annotation to make it known by the system that this class is a domain object
  - **@ID** annotation to mark a field as the primary key
  - **@GeneratedValue** annotation expresses how the autogeneration of the ID should be done
  - **@Version** annotation to mark a field as the optimistic locking field
  - **@NotNull** annotation to mark the fields with not null constraint
  - **@NotEmpty** annotation to mark the field as not blank. Use by the validation engine
  - **@Column** annotation to specify different property for the column like name, type, etc...
  - **@Email** annotation to mark a field as email. The regex is to create a regular expression for the validation engine
  - **@DateCreated** annotation is populated by the engine when a new record is inserted
  - **@DateUpdated** annotation is populated by the engine when a record is updated
- [repository/UserRepository](src/main/groovy/com/example/repository/UserRepository.groovy). 
  This is the user repository. It will handle a lot of CRUD operations to the Users
- service folder. All services under this folder should use the **@Singleton** annotation
  to mark the class as a Service. All services are instantiated upon application start
- [service/security/BCryptPasswordEncoderService](src/main/groovy/com/example/service/security/BCryptPasswordEncoderService.groovy).
  Service class to handle password encoding
- [service/UserService](src/main/groovy/com/example/service/UserService.groovy).
  User service for all database action. This class is where all business logic for
  user should be done. This service uses the following annotations:
  - **@Singleton** annotation marks this class as a singleton class. This means that 
    this object will be instantiated during the application and hence it is considered
    a service that can be injected into other classes.
  - **@Transactional** annotation at the class level makes each method in the class
    as execution within a transaction
- [application.yml](src/main/resources/application.yml). The main configuration file
  for the application. This configuration uses MySQL via a test container. This means
  that there is no need to install a database in the local environment. The test container
  will instantiate a MySQL database and the application will connect to it. 
- [application-test.yml](src/test/resources/application-test.yml). This configuration
  file is used for testing. It uses H2 database for testing. This is a in-memory database
  that is used for testing. The database is created and destroyed for each test

### 2.MySQL
This branch adds a good amount of changes. Mainly, the changes are around using MySQL that 
starts as a container and setup the schema ownership to a specific user. Also, there are
changes for security and JWT. Here is the list of changes:
- [build.gradle](build.gradle). New entries are added to the build file:
  - Security annotation and JWT
  - Reactor for UI
- [run-mysql.sh](run-mysql.sh). This script will start a MySQL container and create a user 
  and database for the application. The script will also create the schema and grant ownership 
  to the user
- [setenv.sh](setenv.sh). This script will set the environment variables for the application
  to use like the database URL, username, and password.
- [setup-schema.sh](setup-schema.sql). This script will setup the database schema and grant
  ownership to the user
- [controller/HelloController](src/main/groovy/com/example/controller/HelloController.groovy). 
  Updated the class to be protected by the security annotation **'@Secured("isAuthenticated()")'** 
  and the GET endpoint is now requesting for the principal to be passed in
- [controller/UserController](src/main/groovy/com/example/controller/UserController.groovy). 
  Updated the class to bypass security using the annotation **'@Secured("isAnonymous()")'**.
  This annotation will let non-authenticated users to access the endpoints
- [domain/Role](src/main/groovy/com/example/domain/Role.groovy). This is a new domain object that will
  contain all the roles int the system. The comment at the end of the class identifies Users that 
  have the role. Currently, that code is commented out since there is no need to have a two-way 
  relationship. Only Users have the link to Roles using a many-to-many relationship reference table.
  This class uses the following annotations: @Entity, @ToString, @Id, @GeneratedValue, @Version, 
  @NotNull, @NotEmpty.
- [domain/User](src/main/groovy/com/example/domain/User.groovy). The User domain is updated to have a 
  Many-to-many relationship with Role since a user can have multiple roles and a role can be assigned
  to multiple users. The new annotation is: **'@ManyToMany(mappedBy = "user", fetch = FetchType.EAGER)'**. 
  This annotation indicates the relationship to Role. The fetch type is set to eager since we want the roles 
  to be loaded when the user is loaded. In addition, we use the **@JoinTable** annotation to specify the
  reference table. The reference table is the intermediate table to make User and Role a
  many-to-many relationship. The **@JoinTable** annotation has the following attributes:
  - **name**. This is the name of the reference table
  - **joinColumns**. This is the column that is used to join the User table to the reference table
  - **inverseJoinColumns**. This is the column that is used to join the Role table to the reference table
  - **uniqueConstraints**. This is used to specify the unique constraint for the reference table
  - **schema**. This is used to specify the schema for the reference table
- [repository/RoleRepository](src/main/groovy/com/example/repository/RoleRepository.groovy). This is the 
  repository for Role. This class uses the following annotations: @Repository, @Transactional
- [repository/UserRepository](src/main/groovy/com/example/repository/UserRepository.groovy). 
  Updated the UserRepository to add a new method: findOneByUsername. This method will find a
  user by the username
- [service/security/AuthProviderService](src/main/groovy/com/example/service/security/AuthProviderService.groovy)
  This is the authentication provider service. This service provides authentication based on the 
  credentials from the DB. Since this class implements the 
  interface **AuthenticationProvider**, Micronaut will automatically use this class for 
  authentication
- [service/RoleService](src/main/groovy/com/example/service/RoleService.groovy). 
  This is the service for Role relation DB activities. For the most part, this class
  insert roles to the database.
  This class uses the following annotations: @Singleton, @Transactional
- [service/UserService](src/main/groovy/com/example/service/UserService.groovy). 
  Added a couple of methods and updated the existing methods
- [resources/application.yml](src/main/resources/application.yml). Updated the configuration file
  to use MySQL directly rather than testcontainers. 
  The configuration file also has the security configuration. The security
  configuration is used to configure the authentication provider and the JWT configuration.
  Notice the $ sign as the value for some of the properties. This is used to get the value from
  the environment variables. The environment variables are set in the script [setenv.sh](setenv.sh).
  Example: **'${JWT_SECRET}'**. This will get the value from the environment variable JWT_SECRET

Before running the application, you need to source the setenv.sh and then start MySQL 
using the script [run-mysql.sh](run-mysql.sh). This script will start a MySQL container 
and create the database and user for the application. Since the script starts the
database as a container, **Docker should be installed** in the local environment using
[Docker Desktop](https://www.docker.com/products/docker-desktop).

```bash
source setenv.sh
./run-mysql.sh
```

After running the command above, the DB will be running and setup with a schema
and a username and password from the [setenv.sh](setenv.sh) script. If you connect
to the DB, you will notice that there is no tables in the schema. This is because
the application will create the tables when it starts. What makes this
possible is the section on JPA in the [application.yml](src/main/resources/application.yml)
file. The property **'jpa.default.properties.hibernate.hbm2ddl.auto'** is set to **'update'**.

```yaml
jpa:
  default:
    properties:
      hibernate:
        hbm2ddl:
          auto: update
```

At this point you can start the application via gradle.

Note: To run all test is still possible without starting MySQL since the tests are using H2
database and 
