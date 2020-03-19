#!/usr/bin/env bash
JAR=$1
PATTERN=$2
#JVM_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -Xss256k -Xmx10g -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc*:gc.log"
JVM_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -Xss256k -Xmx10g -XX:+UnlockExperimentalVMOptions"
EXTRA_OPTS="-Djava.rmi.server.hostname=127.0.0.1"
java \
	$JVM_OPTS \
	$EXTRA_OPTS \
	-Dspring.profiles.active=test \
	-jar "$JAR" \
	pattern="file://$PATTERN"
