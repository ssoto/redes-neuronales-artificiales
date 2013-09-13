#!/bin/bash

# Este script ejecuta el jar sobre todos los archivos .dt
# presentes en el directorio actual.
#
# Para su correcto funcionamiento el directorio actual, 
# relativo al directorio de trabajo, debe ser en el que se 
# encuentren los archivos de entrenamiento (.dt).
#
# Por ejemplo:
#
#  trabajo-redes-neuronales/resources/dataSets/cancer

JAR_PATH="./../../../executables/red-neuronal-v01r06.jar"

for FILE in $(ls ./*.dt); do 
	java -jar $JAR_PATH -dataFile $FILE $@
done

exit 0
