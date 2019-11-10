#!/usr/bin/env bash
JAR=$1
TABLE=$2
PATTERN=$3
JVM_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -Xss256k -Xmx10g -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc*:gc.log"
EXTRA_OPTS="-Djava.rmi.server.hostname=10.245.146.40 -Dcom.sun.management.jmxremote.port=56789 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
java \
	$JVM_OPTS \
	$EXTRA_OPTS \
	-jar "$JAR" \
	LoadLogFromFileToDBJobConfig loadLogFromFileToDB pattern="$PATTERN" table="$TABLE"
