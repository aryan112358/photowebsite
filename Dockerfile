FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/microservices.jar microservices.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "microservices.jar"]