FROM openjdk:21

WORKDIR /app

COPY target/library-management-0.0.1-SNAPSHOT.jar library-management.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "library-management.jar"]
