

# The Hangman Game
This project aims to demonstrate the implementation of the hangman game as explained below.

### 1. Requirements to look for:
* RESTfull
* Spring Boot
* Tests

### 2. Techs and stacks used:
* Maven
* Git repository
* Dev and Test database. H2 console enabled. Item 4.1
* Flyway for migrations
* Lombok to decrease the verbosity of classes
* Model Mapper to avoid the boilerplate between representation and domain models [java-faker](http://modelmapper.org/)
* Unit and integration tests (JUnit and Mockito)
* Dynamic values for tests with [java-faker](https://java-faker.herokuapp.com)
* Swagger for API documentation: http://localhost:8080/swagger-ui.html
* Docker's script to run the app. Item 3.1
* Tests coverage with Jacoco. Item 3.2

### 3. Configs:
1. The file **start-with-docker.sh** runs tests and build the app. Requirements:
- [ ] Java 11
- [ ] Docker
- [ ] Linux/Unix or replace by .bat file

2. Code test coverage with Jacoco. Report can be viewed at: target/site/jacoco/index.html

### 4. How to play:
1. The game will be available at http://localhost:8080
