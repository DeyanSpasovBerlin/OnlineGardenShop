# Use an official OpenJDK 24 runtime as a base image
FROM openjdk:24-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Spring Boot application JAR file from the target folder to the container
#COPY target/OnlineGardenShop-*.jar app.jar
COPY target/OnlineGardenShop-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the Spring Boot application to run on
EXPOSE 8080

# Run the Spring Boot application using the java -jar command
CMD ["java", "-jar", "app.jar"]