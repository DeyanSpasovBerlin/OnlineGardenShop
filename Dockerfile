
FROM openjdk:23-jdk-slim AS builder

WORKDIR /app

COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn/

RUN chmod +x mvnw
COPY src src/

RUN ./mvnw clean package -DskipTests

FROM openjdk:23-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/OnlineGardenShop-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]



