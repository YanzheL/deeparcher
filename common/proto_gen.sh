#!/usr/bin/env sh
SRC_DIR=src/main/kotlin
DST_DIR=$SRC_DIR
PROTO_PKG=com/hitnslab/dnssecurity/deeparcher/api/proto
protoc -I="$SRC_DIR" --java_out="$DST_DIR" $SRC_DIR/$PROTO_PKG/*.proto