# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/smartstudy-pro-*.jar app.jar

# Expose the port your Spring Boot app runs on (usually 8080)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
