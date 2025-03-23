# Start with Ubuntu as the base image
FROM ubuntu:latest

# Install required dependencies
RUN apt update && apt install -y wget tar

# Download and install OpenJDK 23
RUN wget https://download.java.net/java/early_access/jdk23/36/GPL/openjdk-23_linux-x64_bin.tar.gz && \
    tar -xvf openjdk-23_linux-x64_bin.tar.gz -C /opt && \
    rm openjdk-23_linux-x64_bin.tar.gz

# Set environment variables for JDK 23
ENV JAVA_HOME=/opt/jdk-23
ENV PATH=$JAVA_HOME/bin:$PATH

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/OnlineGardenShop-*.jar app.jar

# Expose port 8080 for Spring Boot
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]