FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.config.import=file:/opt/app/application.yml","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]