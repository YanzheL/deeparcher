#!/usr/bin/env sh
SRC_DIR=src/main/kotlin
DST_DIR=$SRC_DIR
PROTO_PKG=com/hitnslab/dnssecurity/deeparcher/api/proto
mkdir -p $SRC_DIR/$PROTO_PKG/generated/python/
mkdir -p $SRC_DIR/$PROTO_PKG/generated/cpp/
protoc -I="$SRC_DIR" \
  --java_out="$DST_DIR" \
  --python_out="$DST_DIR" \
  --cpp_out="$DST_DIR" \
  $SRC_DIR/$PROTO_PKG/*.proto && \
mv $SRC_DIR/$PROTO_PKG/*.py $SRC_DIR/$PROTO_PKG/generated/python/ && \
mv $SRC_DIR/$PROTO_PKG/*.cc $SRC_DIR/$PROTO_PKG/generated/cpp/ && \
mv $SRC_DIR/$PROTO_PKG/*.h $SRC_DIR/$PROTO_PKG/generated/cpp/
