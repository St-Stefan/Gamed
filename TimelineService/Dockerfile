FROM amazoncorretto:21
LABEL authors="bigbucea"

COPY target/TimelineService-0.0.1-SNAPSHOT.jar /app/TimelineService.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/TimelineService.jar"]