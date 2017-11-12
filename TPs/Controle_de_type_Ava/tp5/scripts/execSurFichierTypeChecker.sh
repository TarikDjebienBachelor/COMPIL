#!/bin/bash

# exécution du contrôle de type Ava
# texte à analyser ds un fichier
# nom du fichier en ligne de commande
#
# M. Nebut - 16/12/04

#*******************************
# le répertoire de référence
REP=.
# le répertoire contenant vos .class
CLASSES=$REP/classes
# la classe contenant votre main
MAIN=ava.executeurs.LanceurTypeChecker
#*******************************
java -classpath $CLASSES:${CLASSPATH} $MAIN $1
