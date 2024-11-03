FROM amazoncorretto:21
LABEL authors="bigbucea"

COPY target/UserPageService-0.0.1-SNAPSHOT.jar /app/UserPageService.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/UserPageService.jar"]