# Stage 1: Build stage using JDK 25
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy Maven wrapper files and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Convert line endings of mvnw to Unix format (prevents Windows CRLF start errors)
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Pre-download dependencies (cache layer)
RUN ./mvnw dependency:go-offline

# Copy src and package the jar file skipping tests
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
