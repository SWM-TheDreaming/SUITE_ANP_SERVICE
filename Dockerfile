FROM openjdk:11

ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} suite-anp.jar

ENTRYPOINT ["java","-jar","/suite-anp.jar"]