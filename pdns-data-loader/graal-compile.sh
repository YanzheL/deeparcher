#!/bin/bash
BASH_SOURCE_DIR="$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )"

ARTIFACT=pdns-data-loader
MAINCLASS=com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.PDNSDataLoaderApplicationKt
VERSION=$(cat VERSION)

GRAALVM_VERSION=`native-image --version`
JAR="$ARTIFACT-$VERSION.jar"

echo "Artifact version = ${VERSION}"

rm -rf target/native-image
mkdir -p target/native-image

cd target/native-image \
&& rm -f $ARTIFACT \
&& echo "Unpacking $JAR" \
&& jar -xvf ../$JAR >/dev/null 2>&1 \
&& cp -R META-INF BOOT-INF/classes \
&& LIBPATH=`find BOOT-INF/lib | tr '\n' ':'` \
&& CP=BOOT-INF/classes:$LIBPATH \
&& echo "Compiling $ARTIFACT with $GRAALVM_VERSION" \
&& time native-image \
  --verbose \
  --no-server \
  --no-fallback \
  --initialize-at-build-time \
  -H:Name=$ARTIFACT \
  -H:+ReportExceptionStackTraces \
  --allow-incomplete-classpath \
  --report-unsupported-elements-at-runtime \
  --enable-all-security-services \
  -DremoveUnusedAutoconfig=true \
  --initialize-at-run-time=org.springframework.core.io.VfsUtils \
  -cp "$CP" "$MAINCLASS" \
&& mv $ARTIFACT ../ \
&& echo "Finished"
