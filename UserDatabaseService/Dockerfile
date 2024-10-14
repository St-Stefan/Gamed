# Use a minimal Java runtime for the final image
FROM amazoncorretto:21

# Add a volume to hold the application data
VOLUME /tmp

# The application's jar file
COPY target/UserDatabaseService-0.0.1-SNAPSHOT.jar /app/app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]