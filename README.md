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

### 6-CI
This branch has changes for the CI process. We are using GitHub actions and as so, you
will find a [.github/workflows/ci-build.yaml](.github/workflows/ci-build.yaml). This 
file is use by GitHub to perform Actions upon certain events. 
Changes are as follows:
- [ci-build.yaml](.github/workflows/ci-build.yaml). GitHub Action to perfom a CI build
whenever a merge or PR is done in the dev branch. This means, you need to have a dev
branch where you will merge all your code for integration testing.
This file relies on secrets that need to be setup as part of your repo:  
```bash
# These environment variables come from GitHub Secrets. Configure them!!!
AWS_ACCOUNT_ID: ${{ secrets.AWS_ACCOUNT_ID }}
AWS_REGION: ${{ secrets.AWS_REGION }}
AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
```  
- [build-image.sh](build-image.sh). This file was changed to reflect the environment
variables it will use.
- [ecr-push-image.sh](ecr-push-image.sh). This file was changed to reflect the environment
variable it will use.
- [set-env-vars.sh](set-env-vars.sh). This file was changed to add AWS related env. variables  
- [set-gh-actions-evn-vars.sh](set-gh-actions-env-vars.sh). This file is used by GitHub to set
environment variables in the VM running your workflow

### 7-OpenAPI
This branch offers information about enabling OpenAPI and using the swagger UI to interact
with the available end points. Here is the list of changes:
- [build.gradle](/build.gradle). Add libraries related to OpenAPI and passes configuration for
OpenAPI via the jvm arguments
- [Application.groovy](/src/main/groovy/com/myexample/Application.groovy). Annotation definition
for the project using the OpenAPI annotation to configure the main application
- [ProtectedController](/src/main/groovy/com/myexample/controller/ProtectedController.groovy). 
Controller for protected resource. This controller is now using the OpenAPI annotation to specify
more information about the endpoint
- [application.yml](/src/main/resources/application.yml). Application-wide configuration to enable
OpenAPI, static route to swagger, and allow anonymous access to the swagger folders.
- [security.yml](/src/main/resources/swagger/security.yml). Extra swagger file to expose the login
controller via swagger. This way, the login can also be done via the swagger-ui

### 8-HealthCheck
#### Health Check and Monitoring
Micronaut has a management feature that can be added to the [build.gradle](/build.gradle) file.
Once the library is added, we can enable the management endpoint in the 
[application.yml](/src/main/resources/application.yml) file. Currently, this application
is only enabling the following endpoints:

- /health -> provides health check information about the application
- /info -> specific custom info. Right now, there is nothing sent but you can add your custom information
- /threaddump -> this sends the thread dump for the application

More information and other endpoints can be found in the 
[Micronaut documentation](https://docs.micronaut.io/2.3.1/guide/index.html#management) and
[Micronaut Micrometer with Prometheus](https://micronaut-projects.github.io/micronaut-micrometer/latest/guide/#metricsAndReportersPrometheus).

In addition to management information, this boilerplate is configured to also provide metric information 
when accessing /metrics. However, to get full benefit of it, this application has also enabled the 
[prometheus](https://prometheus.io/) endpoint /prometheus so that prometheus could access the metric information. 
There is no extra configuration or code needed to provide these metrics. To see how the metrics are sent 
to prometheus, a [docker-compose.yaml](/prometheus/docker-compose.yaml) file is setup under 
[/prometheus](/prometheus). The docker compose brings up the following services:

- [prometheus](https://prometheus.io/). Provides metrics and alerting.
- [Node Exporter](https://github.com/prometheus/node_exporter). Provides hardware and OS metrics.
- [cAdvisor](https://github.com/google/cadvisor). Exposes container and hardware statistics to prometheus.
- [Grafana](https://grafana.com/). Graph and dashboard that can connect to prometheus.

The [/prometheus/prometheus yaml](/prometheus/prometheus.yml) configuration would need to change for your environment. 
Currently, the last line uses an IP address that is likely not the same for your development environment. 
You need to change this to your own machines's IP (No, localhost will NOT work).

In order to run the docker compose file, you need to just run it in the command line:

```shell
$ cd prometheus
$ docker-compose up
```
Prometheus URL: http://localhost:9090/

Grafana URL: http://localhost:3000 

Grafana credentials:
- Username: admin
- Password: changeme (setup via env. variable **GF_SECURITY_ADMIN_PASSWORD** from the
  [docker-compose.yaml](/prometheus/docker-compose.yaml) file)

#### Micronaut upgrade
In addition to the management endpoints, this branch upgraded to micronaut 2.5.4. The following are
the files affected:
- [gradle folder](/gradle)
- [build.gradle](build.gradle)
- [gradlew](gradlew)
- [gradlew.bat](gradlew.bat)
- [gradle.properties](gradle.properties)

Other upgrades include the [Dockerfile](Dockerfile) to use Gradle 7 and JDK 11.0.11 