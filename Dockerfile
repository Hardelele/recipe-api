FROM openjdk:11
WORKDIR /app/backend
COPY target/*.jar ./app.jar
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
