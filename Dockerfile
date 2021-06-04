FROM openjdk:11.0.8
ARG JAR_FILE=out/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]