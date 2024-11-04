FROM amazoncorretto:21
LABEL authors="stefanstoicescu"

COPY target/RecommenderService-0.0.1-SNAPSHOT.jar /app/RecommenderService.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/RecommenderService.jar"]