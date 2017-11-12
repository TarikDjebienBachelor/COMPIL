#!/bin/sh

# exécution du contrôle de type Ava
# texte à analyser en ligne
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

echo "Entrez le texte à analyser :"
java -classpath $CLASSES:${CLASSPATH} $MAIN
