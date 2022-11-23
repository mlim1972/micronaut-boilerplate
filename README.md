## Micronaut Boilerplate

This repo is a boilerplate for [Micronaut](https://micronaut.io/). The project was created using 
the [launch button](https://micronaut.io/launch/) in their website. The initial project uses
the following configuration:
- Type: Micronaut Application
  - Micronaut Version: 3.74
- Java Version: 11
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

### 1.MySQL
This branch adds a good amount of changes. The main addition is the usage of databases.
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
  that is used for testing. The database is created and destroyed for each test.