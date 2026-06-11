#!/bin/bash
./mvnw clean package -DskipTests -q
java -Dfile.encoding=UTF-8 -jar target/KuaforOtomasyon-1.0.0.jar
