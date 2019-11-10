#!/usr/bin/env bash
JAR=$1
PATTERN=$2
JVM_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -Xmx12g -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xlog:gc*:gc.log"
java $JVM_OPTS $EXTRA_OPTS -jar "$JAR" LoadLogFromFileToDBJobConfig loadLogFromFileToDB pattern="$PATTERN" table=raw_pdns_log min-pool-size=24 max-pool-size=64
