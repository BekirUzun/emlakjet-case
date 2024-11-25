FROM maven:3.9.9-eclipse-temurin-21-alpine AS base
RUN mkdir -p /workspace
WORKDIR /workspace
COPY .mvn .mvn
COPY mvnw pom.xml /workspace/
RUN mvn dependency:go-offline
COPY src /workspace/src

# test stage
FROM base AS test
RUN mvn test

# build stage
FROM base AS build
RUN mvn clean package

# run stage
FROM eclipse-temurin:21-jre
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]