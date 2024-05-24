FROM openjdk:21

ARG JAR_FILE=./build/libs/howru-2.6.0.jar

COPY ${JAR_FILE} howru.jar

ENTRYPOINT ["java","-jar","/howru.jar"]
