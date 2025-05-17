# Use OpenJDK base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy built jar file into container
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port (optional, if service runs on a specific port)
EXPOSE 8083

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
