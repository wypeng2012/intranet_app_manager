FROM gradle:7.5.1-jdk11 AS builder
WORKDIR /home/gradle/src
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
RUN ./gradlew build
FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY --from=builder ${JAR_FILE} /usr/share/intranet_app_manager.jar
ENTRYPOINT ["java","-jar","/usr/share/intranet_app_manager.jar"]

