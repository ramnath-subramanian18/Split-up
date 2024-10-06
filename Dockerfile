# Use an official OpenJDK 22 runtime as a parent image
FROM openjdk:22-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory to the working directory in the container
COPY target/springboot-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
