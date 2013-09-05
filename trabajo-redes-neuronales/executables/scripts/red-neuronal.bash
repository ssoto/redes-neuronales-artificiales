#!/bin/bash

# Este script ejecuta el .jar pasando como variables
# las pasadas al mismo al jar

JAR_PATH="./../../executables/red-neuronal-v01r05.jar"

java -jar $JAR_PATH $@
