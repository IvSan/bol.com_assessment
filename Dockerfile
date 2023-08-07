FROM openjdk:17-jdk-slim-buster
MAINTAINER ivsan.dev
WORKDIR /app
COPY /build/libs/bol-assessment-0.0.1-SNAPSHOT.jar bol-assessment-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/bol-assessment-0.0.1-SNAPSHOT.jar"]