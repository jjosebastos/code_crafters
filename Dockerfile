# Stage 1: Build

FROM gradle:8.6.0-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${ACTIVE_PROFILE:prod}", "-jar", "app.jar"]