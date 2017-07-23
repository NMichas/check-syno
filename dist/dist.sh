#!/bin/sh
rm *.tar.gz

java -jar lib/packr.jar macos.json
java -jar lib/packr.jar windows.json
java -jar lib/packr.jar linux.json

tar -czf check-syno-linux64.tar.gz check-syno-linux64/*
tar -czf check-syno-macos.tar.gz check-syno-macos/*
tar -czf check-syno-windows64.tar.gz check-syno-windows64/*

rm -rf check-syno-linux64 check-syno-macos check-syno-windows64
