FROM maven:3-jdk-14 AS builder

WORKDIR /app

ARG MAVEN_OPTS

COPY pom.xml .

RUN mvn dependency:go-offline -B $MAVEN_OPTS

COPY . .

RUN mvn package -B -DskipTests -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn

FROM openjdk:15-jdk-slim

LABEL maintainer="Yanzhe Lee"

WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar

ENV JAVA_OPTS "-server -XX:+HeapDumpOnOutOfMemoryError -Xss256k -Xmx10g -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc*:gc.log"

ENV EXTRA_OPTS "-Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote.port=56789 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

ENV JOB_CLASS ""

ENV JOB_NAME ""

ENV JOB_PARAMETERS ""

CMD java $JAVA_OPTS $EXTRA_OPTS -jar app.jar $JOB_CLASS $JOB_NAME $JOB_PARAMETERS
