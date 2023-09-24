FROM openjdk:11

WORKDIR /app

ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} suite-anp.jar

RUN mkdir -p /app/src/main/resources/firebase

COPY src/main/resources/firebase/suite-firebase-admin.json /app/src/main/resources/firebase/

COPY src/main/resources/application.yml /app/src/main/resources/

ENTRYPOINT ["java","-jar","/suite-anp.jar"]