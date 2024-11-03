FROM amazoncorretto:21
LABEL authors="bigbucea"

COPY target/SearchService-0.0.1-SNAPSHOT.jar /app/SearchService.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/SearchService.jar"]