FROM openjdk:11
ARG JAR_FILE=./build/libs/*.jar
VOLUME /allso_Img
COPY ${JAR_FILE} suite-anp.jar
ENTRYPOINT ["java","-jar","/suite-anp.jar"]