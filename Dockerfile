# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Sabse safe tarika jar copy karne ka
COPY --from=build /app/target/*.jar app.jar

# Aapka actual port 8089
EXPOSE 8089

ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
