# Stage 1: Build
FROM eclipse-temurin:17-jre-alpine AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${ACTIVE_PROFILE:prod}", "-jar", "app.jar"]