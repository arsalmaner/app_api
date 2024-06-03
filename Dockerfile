FROM openjdk:17-jdk-slim

WORKDIR /app_api

COPY target/app_api-0.0.1-SNAPSHOT.jar app_api.jar

EXPOSE 8080

CMD ["java", "-jar", "app_api.jar"]