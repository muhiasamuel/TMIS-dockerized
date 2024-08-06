# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the Maven or Gradle build files
COPY pom.xml ./
COPY src ./src

# Install Maven or Gradle
RUN apt-get update && apt-get install -y maven

# Build the application
RUN mvn clean package -DskipTests

# Use a smaller image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]




# # Use an official OpenJDK runtime as a parent image for build stage
# FROM openjdk:17-jdk-slim AS build

# # Set the working directory
# WORKDIR /app

# # Copy the Maven or Gradle build files
# COPY pom.xml ./
# COPY src ./src

# # Install Maven and build the application
# RUN apt-get update && apt-get install -y maven \
#     && mvn clean package -DskipTests \
#     && apt-get remove -y maven \
#     && apt-get clean \
#     && rm -rf /var/lib/apt/lists/*

# # Use a smaller image to run the application
# FROM openjdk:17-jdk-slim

# # Set the working directory
# WORKDIR /app

# # Copy the JAR file from the build stage
# COPY --from=build /app/target/*.jar app.jar

# # Expose port 8080
# EXPOSE 8080

# # Run the JAR file
# ENTRYPOINT ["java", "-jar", "app.jar"]
