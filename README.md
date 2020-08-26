# _Technical Test_
## Goal
Demonstrate code knowledge and skills with Java and Spring Framework to build cloud applications on microservices environments.
## Deliverables
To achieve this goal an application using the technologies above mentioned is been delivered in two formats:
* In a compiling and runnable code, open to be downloaded, used and reviewed by anyone who might interest.
* In a docker image format, ready to be downloaded and ran from Dockerhub.   
#### The code
The code can be reached in a cloud and public repository on Github by the address: https://github.com/eduardomallmann/technical-test.git  
#### The docker image
The docker image can be reached in a cloud and public repository on Dockerhub by the address: https://hub.docker.com/repository/docker/eduardomallmann/technical-test
## The Application
The application has two domains clients and cities interrelated, and can be operated by Rest endpoints.
#### Requirements
* **Java 8**
* **Maven**
##### OR
* **Docker**
* **Linux OS**
#### Running the code
##### From source
To run the code from source, the following steps should be done:
* Build the application with maven and running with java:
```
$ mvn package

$ java -jar target/technical-test.jar
```
* Build and run the application with maven:
```
$ mvn spring-boot:run
```
* Build and run the application with docker:
```
$ docker build -t technical-test .

$ docker run -p 8080:8080 technical-test
```
##### From public docker image
To run the application from its public docker image simply run the code below:
```
$ docker run -p 8080:8080 eduardomallmann/technical-test:0.0.1
```
### API Documentation
To see the API documentation, please run the application and access the address below in the browser:
* http://localhost:8080/swagger-ui.html  
The browser should load an website like the image below:
![Image of Swagger](https://github.com/eduardomallmann/technical-test/blob/master/compasso-swagger.png?raw=true)
