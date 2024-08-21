# Stage 1: Build Stage
FROM 3.9.9-eclipse-temurin-21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the Maven pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/gt-0.0.1-SNAPSHOT.jar /app/gt.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/gt.jar"]

#docker build -t my-springboot-app .
#docker run -p 8080:8080 my-springboot-app
