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
This branch adds a good number of changes. The main addition is the usage of ORM to manage
database actions.
Here is the list of changes:
- [build.gradle](build.gradle). New entries are added to the build file:
  - Micronaut Data Processor. This will work in conjunction with the Micronaut Data and JPA
  - Spring Security Crypto. This library brings the Spring Security Crypto library for password encoding
  - slf4j compatibility layer for logging
  - H2 DB for runtime testing only (commented out in case it is needed)
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
  - **@Serdeable** annotation to make the class serializable. This is important for
    the serialization of the object when it is returned as a response
  - **@ID** annotation to mark a field as the primary key
  - **@GeneratedValue** annotation expresses how the autogeneration of the ID should be done
  - **@Version** annotation to mark a field as the optimistic locking field
  - **@NotEmpty** annotation to mark the field as not blank. Use by the validation engine
  - **@Column** annotation to specify different property for the column like name, type, nullable, etc...
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
    as execution within a transaction.
- [application.properties](src/main/resources/application.properties). The main configuration file
  for the application. This configuration uses MySQL that is started via Docker. So you should start
  mysql before running the application via docker compose.
- [application-test.properties](src/test/resources/application-test.properties). This configuration
  file is used for testing. It uses test containers to start a MySQL database for testing purposes.
  This can be changed to use H2 if needed. Uncomment the H2 dependency in the build.gradle file and
  change the datasource to use H2 and comment the MySQL datasource.
- [docker-compose.yml](docker-compose.yml). This file is used to start the MySQL database
  for development. The database is started with the following configuration:
  - Database name: myappdb
  - User: myappuser
  - Password: myappuser_secret_password
  - Port: 3306

### 2.MySQL
This branch adds a good number of changes. Mainly, the changes are around using MySQL that 
starts as a container and setup the schema ownership to a specific user. Also, there are
changes for security and JWT. Here is the list of changes:
- [build.gradle](build.gradle). New entries are added to the build file:
  - Security annotation and JWT
  - Reactor
- [run-mysql.sh](run-mysql.sh). This script will start a MySQL container and create a user 
  and database for the application. The script will also create the schema and grant ownership 
  to the user
- [rest-test.http](rest-test.http). This file contains the test cases for the
  running application. The test cases are written in HTTP format and can be run using the 
  native IntelliJ UI.
- [setenv.sh](setenv.sh). This script will set the environment variables for the application
  to use like the database URL, username, and password.
- [setup-schema.sh](setup-schema.sql). This script will setup the database schema and grant
  ownership to the user. it gets executed by the run-mysql.sh script.
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
- [resources/application.yml](src/main/resources/application.properties). Updated the configuration file
  to add security configuration. The security
  configuration is used to configure the authentication provider and the JWT configuration.
  Notice the $ sign as the value for some properties. This is used to get the value from
  the environment variables. The environment variables are set in the script [setenv.sh](setenv.sh).
  Example: **'${JWT_SECRET}'**. This will get the value from the environment variable JWT_SECRET

Before running the application, you need to source the setenv.sh and then start MySQL 
using the script [run-mysql.sh](run-mysql.sh). This script will start a MySQL container 
and create the database and user for the application. Since the script starts the
database as a container, **Docker should be installed** in the local environment using
[Docker Desktop](https://www.docker.com/products/docker-desktop).

```bash
./run-mysql.sh
```

After running the command above, the DB will be running and setup with a schema
and a username and password from the [setenv.sh](setenv.sh) script. If you connect
to the DB, you will notice that there are no tables in the schema. This is because
the application will create the tables when it starts. What makes this
possible is the section on JPA in the [application.properties](src/main/resources/application.properties)
file. The property **'jpa.default.properties.hibernate.hbm2ddl.auto'** is set to **'update'**.

```properties
jpa.default.properties.hibernate.show_sql=false
jpa.default.properties.hibernate.format_sql=false
jpa.default.properties.hibernate.hbm2ddl.auto=update
```

At this point you can start the application via Gradle.

Note: All tests run against test containers. The test containers will start a MySQL
database via testcontainers. 

### 2.Flyway
[Flyway](https://flywaydb.org/) is a database migration tool. It is used to manage the database schema and data.
The application will use Flyway to create the database schema and insert seed data.
Using Flyway will make the application more portable since the database schema and data
will be created when the application starts. This will make it easier to deploy the
application to different environments. In addition, as we add new features to the application
and the database schema changes, Flyway will be used to update the database schema. 
Flyway creates a table in the database to keep track of the changes. The table is called
**'flyway_schema_history'**. This table is used to keep track of the changes that have been
applied to the database. The table has the following columns:
- installed_rank. This column is used track the order of changes
- version. This column is used track the version of changes
- description. This column is used to track the description of changes
- type. This column is used to track the type of changes
- script. This column is used to track the script used to make changes
- checksum. This column is used to track the checksum of the changes
- installed_by. This column is used to track the user that installed the changes
- installed_on. This column is used to track the date and time when the changes were installed
- execution_time. This column is used to track the execution time of the changes
- success. This column is used to track the success of the changes

The following files are changed to this branch:
- [README.md](README.md). This file was updated to add information about Flyway
- [setenv.sh](setenv.sh). Updated the MYSQL URL to include a new parameter needed to connect
     to the MYSQL v8.0. The parameter is **'allowPublicKeyRetrieval=true'**
- [/domain/Role](src/main/groovy/com/example/domain/Role.groovy). This class was updated to
     add the following annotation **@EqualsAndHashCode**. This annotation is used to generate
     the equals and hashcode methods
- [/domain/User](src/main/groovy/com/example/domain/User.groovy). This class was updated to
     add the following annotation **@EqualsAndHashCode**. This annotation is used to generate
     the equals and hashcode methods
- [/domain/UserRole](src/main/groovy/com/example/domain/UserRole.groovy). This class was updated to
     add the following annotation **@EqualsAndHashCode**. This annotation is used to generate
     the equals and hashcode methods
- [/domain/UserRoleKey](src/main/groovy/com/example/domain/UserRoleKey.groovy). This class was updated to
     add the following annotation **@EqualsAndHashCode**. This annotation is used to generate
     the equals and hashcode methods
- [/controller/UserController](/src/main/groovy/com/example/controller/UserController.groovy). 
  The getUsers method was updated to receive the page and the page size as parameters
- [/service/UserService](/src/main/groovy/com/example/service/UserService.groovy). This class was updated
     to change the **list** method to use Pageable
- [application.properties](/src/main/resources/application.properties). Added a parameter to configure
     Flyway to manage the database schema. Added connection parameters for the initial and max pool size.
     We will create DDL schema manually, hence the 
     **jpa.default.property.hibernate.hbm2ddl.auto** is set from **update** to **none**.
```properties
jpa.default.properties.hibernate.hbm2ddl.auto=none
```
- [/resources/dbmigrations/V1__setting_userandrole.sql](src/main/resources/dbmigrations/V1__setting_userandrole.sql). 
  This file contains the SQL to create the initial database schema. Once this is deployed, this file will not be
  modified. New changes to the database schema will be added to new files. The files will be named in the format
    **V{version}__{description}.sql**. The version is a number used to track of the order changes.
  This file in particular sets up the database schema and inserts seed data. So, it creates the following tables:
    - role
    - user
    - user_role
  
  This file also seeds the table with initial data to the role table. The initial data is the following:
    - ADMIN
    - USER
    - EDITOR
- [/dbmigrationscript/v1/V1_1__seed_user](src/main/resources/db/migration/v1/V1_1__seed_user.sql).
  This is the script to insert seed data for the user table and the user role table. Flyway
  will run the .sql file above and this file to create the database schema and insert
  seed data. The reason we need to create a groovy script rather than adding it to the Flyway .sql file
  is that we need to use code that cannot be performed in .sql file. The groovy script
  adds a user to the user table using the encrypted password. Encryption is not available in SQL.
- [/controller/LoginControllerSpec](/src/test/groovy/com/example/controller/LoginControllerSpec.groovy). 
  This class was updated to change the index when inserting to tables. This is done so that while testing
  there is no conflict with previous data that was inserted to the tables
- [/controller/UserControllerSpec](/src/test/groovy/com/example/controller/UserControllerSpec.groovy).
  This class was updated to change the index when inserting to tables. This is done so that while testing
  there is no conflict with previous data that was inserted to the tables. In addition, the test was updated 
  to test the list of users by page and size
- [/service/RoleServiceSpec](/src/test/groovy/com/example/service/RoleServiceSpec.groovy). 
  This class was updated to change the authority name because the 'admin' authority was already inserted
  by flyway during the initial setup of the database schema
- [/service/UserServiceSpec](/src/test/groovy/com/example/service/UserServiceSpec.groovy). 
  This class was updated to change the index when inserting to tables. This is done so that while testing
  there is no conflict with previous data that was inserted to the tables

Since we are creating a new flow for db creation, the [run-mysql.sh](run-mysql.sh) script should be run
to create the new DB schema via Flyway.

### 4.Containerization
This branch adds the containerization of the application. The application is containerized using Docker.
The following files are added to this branch:
- [.dockerignore](.dockerignore). This file is used to ignore files when creating the Docker image
- [build-image.sh](build-image.sh). This file is a reference script used to build the Docker image for the 
  application. This file helps understand the creation of the docker image. The Docker image is created 
  using the Dockerfile. The **build-image.sh** will likely get called by the CI/CD pipeline. Take note that
  the test and the build are separated because the tests use testcontainers to start a MySQL database and
  using Testcontainers during the image creation was causing issues due to docker in docker
- [Dockerfile](Dockerfile). This file is used to create the Docker image for the application. The Dockerfile 
  receives environment variables and are mapped via **ARG**. The **build-image.sh and run-image.sh** scripts 
  will pass the environment variables during the build and run of the container respectively. The
  image used for creating the application image is a distroless image. The distroless image is a minimal
  image that only contains the application and its dependencies. The distroless image used is the
  [gcr.io/distroless/java17-debian11](https://github.com/GoogleContainerTools/distroless/tree/main)
- [ecr-push-image.sh](ecr-push-image.sh). This file is a reference script used to push the Docker image to 
  the AWS ECR repository. The **ecr-push-image.sh** will likely get called by the CI/CD pipeline
- [run-image.sh](run-image.sh). This file is a reference script used to run the Docker image for the 
  application. This file helps understand the running of the docker image. This script should be called 
  after the **build-image.sh**. The **run-image.sh** will likely get called by the CI/CD pipeline
- [run-mysql.sh](run-mysql.sh). This file was changed to use the new environment variables set by **set-env.sh**. 
- [set-env.sh](set-env.sh). This file was changed to use more descriptive environment variables. In addition, 
  the **set-env.sh** script now detects the OS and set the **hostIP** accordingly. Windows is not supported directly,
  so, Windows users should use WSLv2 to use this project
- [setup-schema.sql](setup-schema.sql). This file was changed to use the new environment variables set 
  by **set-env.sh**
- [application.yml](/src/main/resources/application.yml). This file was changed to use the new environment 
  variables set by **set-env.sh**. In addition, the server port is configurable. The Dockerfile can set the new
  port number
- [controller/UserController](/src/main/groovy/com/example/controller/UserController.groovy). 
  This class was changed because there was an issue with the initial page. The initial pages was
  set to 0, but the page number should start at 1. So, the initial page was changed to 1
- [service/UserService](/src/main/groovy/com/example/service/UserService.groovy). 
  This class was changed to add more comments about how pagination works with sorting
- [controller/UserControllerSpec](/src/test/groovy/com/example/controller/UserControllerSpec.groovy). 
  This class was changed from page 1 to page 0 because the initial page should be 0

### 5.JWT
Changes to this branch are to add JWT authentication to the application. The initial application already had the
libraries associated to JWT and there was a login test that was working. Changes to the application were made
to make the JWT integration smoother and to add more tests. The following files were added to this branch:
- [controller/HelloController](/src/main/groovy/com/example/controller/HelloController.groovy). 
  This class was changed so that it would have a public and private endpoint. The public endpoint is
  the **/hello** endpoint and the private endpoint is the **/hello/private** endpoint. The private endpoint
  requires a JWT token to be passed in the header. The JWT token is generated by the login endpoint
- [controller/HelloControllerSpec](/src/test/groovy/com/example/controller/HelloControllerSpec.groovy).
  This class was added to test the **/hello** and **/hello/private** endpoints. Both endpoints are tested
  without authenticating first. The **/hello/private** endpoint is tested to return a 401 status code
  because the user is not authenticated. 
- [service/security/AuthProviderService](/src/main/groovy/com/example/service/security/AuthProviderService.groovy).
  This class was changed to add more comments to all methods. The **authenticate** method is called
  by the **JwtAuthenticationFilter** to authenticate the user. The **authenticate** method will call
  the **findByUsername** to find the user in the database. If the user is valid, the method checks the state
  of the user and if the user is active, the method will return a **AuthenticationResponse** that is successful.
- [application.properties](/src/main/resources/application.properties). This file was changed to add the JWT expiration and
  to explicitly disabling cookie based authentication. The JWT expiration is set to 5 hours
- [client/AppClient](/src/main/groovy/com/example/client/AppClient.groovy). This is a new interface that represents
  a declarative use of the HTTP Client in micronaut. This interface is used by the 
  **DeclarativeHttpClientWithJwtSpec** to call the login and authenticate via this declarative interface. 
  The non-declarative way is still valid via the normal **HttpClient** class via **HelloControllerSpec**.
- [controller/DeclarativeHttpClientWithJwtSpec](/src/test/groovy/com/example/controller/DeclarativeHttpClientWithJwtSpec.groovy).
  This class was added to test the **AppClient** interface. The **AppClient** interface is used to call the login
  and authenticate endpoints. The **AppClient** interface is used to call the login endpoint to get a JWT token.
  The JWT token is then used to call the **/hello/private** endpoint
- [controller/LoginControllerSpec](/src/test/groovy/com/example/controller/LoginControllerSpec.groovy).
  This class was changed to add the protected endpoint test. The protected endpoint test calls the **/hello/private**

JWT information can be found in this great article: 
https://guides.micronaut.io/latest/micronaut-security-jwt-gradle-groovy.html 

The article above also covers how to generate a refresh token. The refresh token is not implemented in this project.

### 6.JsonView
This branch adds the JsonView feature to the application. The JsonView feature is used to control which 
properties of a Domain object are serialized into JSON when returning a response from a controller. 
This is useful when you want to expose different levels of detail to different clients or roles.

The following files were added or modified in this branch:

- [ApiView.groovy](/src/main/groovy/com/example/jsonview/ApiView.groovy): 
  This file defines the different JsonView classes. In this example, there are two views: `Public` and `Internal`. 
  The `Internal` view extends the `Public` view, meaning it includes all the properties of the `Public` view plus 
  some additional properties.
- [User.groovy](/src/main/groovy/com/example/domain/User.groovy): The `User` domain class is updated to use 
  the `@JsonView` annotation. This annotation specifies which view a property should be included in. 
  For example, the `password` property is only included in the `Internal` view, while the `firstName`, `lastName`, 
  and `username` properties are included in the `Public` view.
- [Role.groovy](/src/main/groovy/com/example/domain/Role.groovy): The `Role` domain class is updated to use 
  the `@JsonView` annotation. This annotation specifies which view a property should be included in. 
  For example, the `version` property is only included in the `Internal` view.
- [UserController.groovy](/src/main/groovy/com/example/controller/UserController.groovy): The `UserController` is 
  updated to use the `@JsonView` annotation on the controller methods. This annotation specifies which view should be 
  used when serializing the response. For example, the `getUsers` and `getUser` methods use the `ApiView.Public.class` 
  view, while other methods could use the `ApiView.Internal.class` if they existed and needed to expose internal 
  properties.
- [application.properties](/src/main/resources/application.properties): The `micronaut.serde.json-view-enabled` 
  property is set to `true` to enable JsonView support.
- [README.md](README.md): The README file is updated to include information about the JsonView feature and how to use it.

With these changes, the application can now control which properties are serialized into JSON based on the specified view. This allows for more fine-grained control over the data that is exposed to clients.

### 7.CI
This branch adds the CI pipeline to the application. The CI pipeline is used to build, test, and deploy the application.
The CI pipeline is configured using GitHub Actions. The workflow file is located 
at '[.github/workflows/ci.build.yaml](.github/workflows/ci.build.yaml)'. This workflow is triggered on pushes and 
pull requests to the `dev` branch.

The workflow performs the following steps:

1.  **Checkout**: Checks out the code from the repository.
2.  **Set environment variables**: Sets environment variables required for the build, including AWS credentials 
and other configuration parameters. It sources `set-env-vars.sh` and `set-gh-actions-env-vars.sh` to set these variables.
3.  **Start MySQL db server and set up schema**: Starts a MySQL database server using Docker and sets up the database 
schema. It uses the `run-mysql.sh` script for this purpose.
4.  **Build and tag image**: Builds the Docker image using the `build-image.sh` script and tags it.
5.  **Push to ECR**: Pushes the Docker image to Amazon ECR using the `ecr-push-image.sh` script.

**Environment Variables**:

The workflow uses several environment variables that are configured as GitHub Secrets. These include:

*   `AWS_ACCOUNT_ID`: The AWS account ID.
*   `AWS_REGION`: The AWS region.
*   `AWS_ACCESS_KEY_ID`: The AWS access key ID.
*   `AWS_SECRET_ACCESS_KEY`: The AWS secret access key.

These secrets must be configured in the GitHub repository settings for the workflow to function correctly.

**Scripts Used**:

*   `set-env-vars.sh`: Sets environment variables for local development and CI.
*   `set-gh-actions-env-vars.sh`: Sets environment variables specifically for GitHub Actions, 
making them available to subsequent steps.
*   `run-mysql.sh`: Starts a MySQL container, creates the database, and sets up the schema.
*   `build-image.sh`: Builds the Docker image for the application.
*   `ecr-push-image.sh`: Pushes the Docker image to Amazon ECR.

This CI pipeline automates the build, test, and deployment process, ensuring that changes to the `dev` branch 
are automatically built and deployed to Amazon ECR.

### 8.OpenAPI
This branch adds OpenAPI support to the application. The OpenAPI support is used to generate API documentation
for the application. The OpenAPI documentation is generated using the Micronaut OpenAPI library.
The following files were added or modified in this branch:

- [Application.groovy](/src/main/groovy/com/example/Application.groovy):
  - Added `@OpenAPIDefinition` annotation to define the OpenAPI documentation. This includes metadata such as title, version, description, license, and contact information.
  - Added `@OpenAPIInclude` annotation to include the `LoginController` in the OpenAPI documentation and tag it as "Security".
  - Added `@SecurityScheme` annotation to define the security scheme for JWT authentication, specifying the type as HTTP, scheme as bearer, and bearer format as JWT.
- [ProtectedController.groovy](/src/main/groovy/com/example/controller/ProtectedController.groovy):
  - Added `@SecurityRequirement(name = "BearerAuth")` annotation to specify that the endpoint requires JWT authentication.
  - Added `@Operation`, `@ApiResponse`, `@Content`, `@Schema`, and `@Tag` annotations to provide detailed information about the API endpoint for the OpenAPI documentation.
- [build.gradle](build.gradle):
  - Added dependencies for Micronaut OpenAPI: `io.micronaut.openapi:micronaut-openapi` and `io.micronaut.openapi:micronaut-openapi-annotations`.
  - Added `annotationProcessor` for `io.micronaut.openapi:micronaut-openapi`.
  - Added `compileOnly` for `io.micronaut.openapi:micronaut-openapi-annotations`.
  - Added JVM arguments to the `GroovyCompile` task to enable RapiDoc and Swagger UI and to specify additional files for OpenAPI.
- [application.properties](src/main/resources/application.properties):
  - Added configurations to enable and configure static resources for Swagger UI, RapiDoc, and ReDoc.
  - Added security intercept URL maps to allow anonymous access to Swagger UI, RapiDoc, and ReDoc endpoints.
- [openapi.properties](openapi.properties):
  - Added `swagger-ui.enabled=true` to enable Swagger UI.
- [SwaggerUiSpec.groovy](/src/test/groovy/com/example/swagger/SwaggerUiSpec.groovy):
  - Added a test to verify that the Swagger UI is available and returns a 200 OK status.
- [OpenApiGeneratedSpec.groovy](/src/test/groovy/com/example/swagger/OpenApiGeneratedSpec.groovy):
  - Added a test to verify that the OpenAPI specification is generated and valid.

### 9-HealthCheck
#### Health Check and Monitoring
Micronaut has a management feature that can be added to the [build.gradle](/build.gradle) file.
Once the library is added, we can enable the management endpoint in the
[application.properties](/src/main/resources/application.properties) file. Currently, this application
is only enabling the following endpoints:

- /health -> provides health check information about the application
- /info -> specific custom info. Right now, there is nothing sent, but you can add your custom information
- /threaddump -> this sends the thread dump signal for the application

More information and other endpoints can be found in the
[Micronaut documentation](https://docs.micronaut.io/latest/guide/index.html#management) and
[Micronaut Micrometer with Prometheus](https://micronaut-projects.github.io/micronaut-micrometer/latest/guide/#metricsAndReportersPrometheus).

In addition to management information, this boilerplate is configured to also provide metric information
when accessing /metrics. However, to get the full benefit of it, this application has also enabled the
[prometheus](https://prometheus.io/) endpoint /prometheus so that Prometheus could access the metric information.
There is no extra configuration or code needed to provide these metrics. To see how the metrics are sent
to prometheus, a [docker-compose.yml](/prometheus/docker-compose.yml) file is set up under the
[/prometheus](/prometheus) folder. The docker compose brings up the following services:

- [prometheus](https://prometheus.io/). Provides metrics and alerting.
- [Node Exporter](https://github.com/prometheus/node_exporter). Provides hardware and OS metrics.
- [cAdvisor](https://github.com/google/cadvisor). Exposes container and hardware statistics to prometheus.
- [Grafana](https://grafana.com/). Graph and dashboard that can connect to prometheus.

The [/prometheus/prometheus yml](/prometheus/prometheus.yml) configuration would need to change for your environment.
Currently, the last line uses an IP address that is likely different from your development environment.
You need to change this to your own machine's IP (No, localhost will NOT work).

To run the docker compose file, you need to just run it in the command line. Remember to source the 
[setenv.sh](setenv.sh) file first to set the environment variables.

```shell
$ source setenv.sh
$ cd prometheus
$ docker-compose up
```
Prometheus URL: http://localhost:9090/

Grafana URL: http://localhost:3000

Grafana credentials:
- Username: admin
- Password: changeme (setup via env. variable **GF_SECURITY_ADMIN_PASSWORD** from the
  [docker-compose.yml](/prometheus/docker-compose.yml) file)

File changes:
- [build.gradle](/build.gradle). Added the Micronaut management library to the dependencies.
- [application.properties](/src/main/resources/application.properties). 
  Added the management endpoints and enabled the prometheus endpoint.
- [ExampleInfoSource.groovy](/src/main/groovy/com/example/ExampleInfoSource.groovy).
  This is an example of how to add custom information to the /info endpoint. This is not used in the application,
  but it is a good example of how to add custom information.
- [prometheus/docker-compose.yml](/prometheus/docker-compose.yml).
  This file is used to run the prometheus, node exporter, cAdvisor, and Grafana services.
- [prometheus/prometheus.yml](/prometheus/prometheus.yml).
  This file is used to configure the prometheus service. It specifies the targets to scrape metrics from.

### JMXExporter
This branch also has changes that add a java agent, ```JMX Exporter``` which runs along with the Java process and 
serves metrics of the local JVM. The [run-image.sh](run-image.sh) script is updated to expose the JMX port, 9301.
JMX can be used to check whether the service is up or not by having prometheus scrape the metrics and have some 
monitoring on the GC events, threads, etc.
To configure JMX, we need to have a jmx prometheus agent jar file and a configuration file which contains 
information about what metrics to serve specifically excluding the rest.

To run the java agent, we added the following data into JAVA_OPS in [Dockerfile](Dockerfile):
```
-javaagent:jmx_prometheus_javaagent-1.3.0.jar=9103:jmx_config.yml
```

File changes:
- [Dockerfile](Dockerfile). Added the JMX Exporter jar file and the configuration file to the Docker image.
- [jmx_config.yml](jmx_config.yml). This is the configuration file for the JMX Exporter. 
  It specifies which metrics to expose and how to format them. The configuration file by the JMX Exporter
  to scrape metrics from the JVM and expose them in a format that Prometheus can understand.
- [jmx_prometheus_javaagent-1.3.0.jar](https://github.com/prometheus/jmx_exporter). 
  This is the JMX Exporter jar file used to expose the JVM metrics. The jar file is added to the Docker image
  and is used by the JMX Exporter to scrape metrics from the JVM.
- [run-image.sh](/run-image.sh).
  This file is updated to include the jmx management port. The management port is set to 9103.

More information about JMX Exporter:
[JMX Exporter](https://github.com/prometheus/jmx_exporter).