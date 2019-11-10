#!/usr/bin/env bash
JAR=$1
TABLE=$2
PATTERN=$3
JVM_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -Xss256k -Xmx10g -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc*:gc.log"
java \
	$JVM_OPTS \
	$EXTRA_OPTS \
	-jar "$JAR" \
	LoadLogFromFileToDBJobConfig loadLogFromFileToDB pattern="$PATTERN" table="$TABLE"
