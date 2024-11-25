# Emlakjet E-commerce Case

## Tech Stack
- Java 21 with Maven 3.9.9
- Spring Boot Starter 3.3.6 (Spring Core 6.1.13)
- MongoDB 7
- Docker

## Getting Started

### Run with Docker
- Install and start [Docker](https://www.docker.com/)
- Build backend image with ``docker compose build``
- Start MongoDB and backend containers with ``docker compose up``

### Debug with an IDE
- Install [Maven](https://maven.apache.org/)
- Install [MongoDB 7](https://www.mongodb.com/) and start it with ``bin/mongod``
- Import project as Maven project to your IDE
- Run ``mvn clean package``
- Run ``EcommerceApplication`` with ``--spring.profiles.active=local`` program argument or use the run config provided for IntelliJ Idea

After starting the application with either an IDE or Docker, Swagger UI should be available at http://localhost:8080/api/swagger-ui/index.html

### Tests
Run ``mvn clean test`` to run the tests or use ``All tests`` run configuration provided for IntelliJ Idea