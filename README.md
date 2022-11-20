<style>
figure {
  text-align: center;
  font-style: italic;
  font-size: smaller;
}
</style>

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

<figure>
<img alt="Launch Configuration" src="images/Micronaut_Launch.png">
<figcaption ><b>Fig.1 - Launch Configuration</b></figcaption>
</figure>

This boilerplace will be a guide to many features microservices use like database
connectivity, containers, authentication, OpenAPI, tracing, etc... 
Those features will be added as branches that build on top of the 
previous one.
Comparing branches can be done by initiating the creation of a Pull Request and 
comparing the branches.

<figure>
<img alt="Pull Request branch comparison" src="images/github-compare.png">
<figcaption><b>Fig.2 - Pull Request branch comparison</b></figcaption>
</figure>

## Branches
### main
The main branch is the initial project configured as mentioned above and following the 
[CREATING YOUR FIRST MICRONAUT APPLICATION](https://guides.micronaut.io/latest/creating-your-first-micronaut-app-gradle-groovy.html)
guide with a few changes:
- Used Launch Button from the website rather than using the CLI
- [HelloController](src/main/groovy/com/example/controllers/HelloController.groovy) is in a folder called controllers, so we can add all our controllers here
- Added a service class [HelloService](src/main/groovy/com/example/services/HelloService.groovy) under the services folder to store all our services
- Created [controllers](src/test/groovy/com/example/controllers) and 
  [services](src/test/groovy/com/example/services) test files 
  for their corresponding classes