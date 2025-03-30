FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/zap-url-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]