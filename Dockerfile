# Stage 1: Build stage
FROM maven:3.8.8-amazoncorretto-21 AS build
WORKDIR /app

# Copy only necessary files for dependency resolution
COPY pom.xml ./
COPY src ./src
RUN mvn -B -f pom.xml clean package -DskipTests

# Stage 2: Run stage
FROM amazoncorretto:21-alpine3.18
WORKDIR /app

# Copy the application JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
