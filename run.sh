#!/usr/bin/env bash
JAR=$1
PATTERN=$2
JVM_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -Xmx5000m -XX:+UnlockExperimentalVMOptions -XX:+UseZGC"
java $JVM_OPTS -jar "$JAR" LoadLogFromFileToDBJobConfig loadLogFromFileToDB pattern="$PATTERN"