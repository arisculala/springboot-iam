FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/trading-app.jar trading-app.jar
ENTRYPOINT ["java", "-jar", "trading-app.jar"]
