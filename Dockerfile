# Build
FROM eclipse-temurin:21-jdk-alpine AS build
RUN apk add --no-cache maven
WORKDIR /app

COPY pom.xml .
COPY src src
RUN mvn -B -DskipTests clean package

# Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/hortifruti-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
