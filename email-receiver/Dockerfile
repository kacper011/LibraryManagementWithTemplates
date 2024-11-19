FROM openjdk:21

WORKDIR /app

COPY target/email-receiver-0.0.1-SNAPSHOT.jar email-receiver.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "email-receiver.jar"]