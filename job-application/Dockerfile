FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

ARG JAR_FILE=target/job-application-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
