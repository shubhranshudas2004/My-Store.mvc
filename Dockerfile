# Stage 1: Build the project using Maven
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/myapp-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Start the app
CMD ["java", "-jar", "app.jar"]
