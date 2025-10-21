# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY pom.xml .
COPY src ./src

# Make mvnw executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port (dynamic port handled in properties)
EXPOSE 1122

# Start the Spring Boot app
CMD ["java", "-jar", "target/myapp-0.0.1-SNAPSHOT.jar"]
