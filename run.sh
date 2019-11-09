#!/usr/bin/env bash
JAR=$1
PATTERN=$2
JVM_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -Xmx4g -XX:+UnlockExperimentalVMOptions -XX:+UseZGC"
java $JVM_OPTS -jar "$JAR" LoadLogFromFileToDBJobConfig loadLogFromFileToDB pattern="$PATTERN" table=raw_pdns_log