FROM maven:3-jdk-14 AS builder

WORKDIR /app

ARG MAVEN_OPTS

COPY pom.xml .

RUN mvn dependency:go-offline -B $MAVEN_OPTS -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn

COPY . .

RUN mvn package -B -DskipTests

FROM openjdk:15-jdk-slim

LABEL maintainer="Yanzhe Lee"

WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar

ENV JVM_OPTS "-server -Xmx4g -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc*:gc.log"

ENV EXTRA_OPTS ""

ENV JOB_PARAMETERS ""

USER 1001

CMD java $JVM_OPTS $EXTRA_OPTS -jar app.jar $JOB_PARAMETERS
