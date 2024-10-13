FROM amazoncorretto:21
LABEL authors="bigbucea"

COPY target/ReviewWriterService-0.0.1-SNAPSHOT.jar /app/ReviewWriterService.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/ReviewWriterService.jar"]