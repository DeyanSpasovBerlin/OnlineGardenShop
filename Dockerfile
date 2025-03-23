## Use an official OpenJDK 24 runtime as a base image
#FROM openjdk:24-jdk-slim
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the compiled Spring Boot application JAR file from the target folder to the container
##COPY target/OnlineGardenShop-*.jar app.jar
#COPY target/OnlineGardenShop-0.0.1-SNAPSHOT.jar app.jar
#
## Expose port 8080 for the Spring Boot application to run on
#EXPOSE 8080
#
## Run the Spring Boot application using the java -jar command
#CMD ["java", "-jar", "app.jar"]
# Use OpenJDK 24 as the base image
#******************************
FROM openjdk:24-jdk-slim AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and POM file
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Grant execute permissions for Maven wrapper (needed in Linux-based containers)
RUN chmod +x mvnw

# Copy the source code into the container
COPY src src

# Build the application inside the container
RUN ./mvnw clean package -DskipTests

# Use a new, clean runtime image to run the app
FROM openjdk:24-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/OnlineGardenShop-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]