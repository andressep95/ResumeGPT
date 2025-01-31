# Build stage
FROM maven:3.9-amazoncorretto-17 AS builder
WORKDIR /app

# Copy only the POM file first
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Run stage
FROM amazoncorretto:17-alpine
WORKDIR /app

# Add non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Create directory for temporary files
RUN mkdir /app/temp && chown spring:spring /app/temp

# Copy the built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar
COPY --chown=spring:spring src/main/resources/static /app/static

# Set permissions
RUN chown spring:spring app.jar
USER spring:spring

# Expose the application port
EXPOSE 8080

# Set temporary directory as volume
VOLUME /app/temp

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]