# Use a base image with Java installed
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/rest-service-*.jar ./rest-service.jar

# Expose the port on which the application will run
EXPOSE 8085

# Set the command to run the application
CMD ["java", "-jar", "rest-service.jar"]