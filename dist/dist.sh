#!/bin/bash

# Building
echo Building CheckSyno
mvn clean install -q -f ../pom.xml

# Fetching JDKs
if [ ! -f lib/openjdk-1.7.0-u80-unofficial-windows-amd64-image.zip ]; then
	echo Fetching Windows JDK
	wget -q https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/openjdk-1.7.0-u80-unofficial-windows-amd64-image.zip -O lib/openjdk-1.7.0-u80-unofficial-windows-amd64-image.zip
fi
if [ ! -f lib/openjdk-1.7.0-u80-unofficial-macosx-x86_64-image.zip ]; then
	echo Fetching MacOS JDK
	wget -q https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/openjdk-1.7.0-u80-unofficial-macosx-x86_64-image.zip -O lib/openjdk-1.7.0-u80-unofficial-macosx-x86_64-image.zip
fi
if [ ! -f lib/openjdk-1.7.0-u80-unofficial-linux-amd64-image.zip ]; then
	echo Fetching Linux JDK
	wget -q https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/openjdk-1.7.0-u80-unofficial-linux-amd64-image.zip -O lib/openjdk-1.7.0-u80-unofficial-linux-amd64-image.zip
fi

# Fetching Packr
if [ ! -f lib/packr.jar ]; then
	echo Fetching Packr
	wget -q http://bit.ly/packrgdx -O lib/packr.jar
fi

# Removing previous binaries
echo Removing previous binaries
rm -f syno-check-*.tar.gz

# Packaging binaries
echo Preparing MacOS binary
java -jar lib/packr.jar macos.json
echo Preparing Windows binary
java -jar lib/packr.jar windows.json
echo Preparing Linux binary
java -jar lib/packr.jar linux.json

# Packaging
echo Packaging MacOS binary
tar -czf check-syno-macos.tar.gz check-syno-macos/*
echo Packaging MacOS binary
tar -czf check-syno-windows64.tar.gz check-syno-windows64/*
echo Packaging Linux binary
tar -czf check-syno-linux64.tar.gz check-syno-linux64/*

# Removing build artifacts
echo Removing build artifacts
rm -rf check-syno-linux64 check-syno-macos check-syno-windows64
