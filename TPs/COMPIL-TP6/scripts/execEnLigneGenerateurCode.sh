#!/bin/sh

# exécution du générateur de code Ava
# texte à analyser en ligne
#
# M. Nebut - 16/12/04


#*******************************
# le répertoire de référence
REP=.
# le répertoire contenant vos .class
CLASSES=$REP/classes
# la classe contenant votre main
MAIN=ava.executeurs.LanceurGenerateurCode

#*******************************

echo "Entrez le texte à analyser :"
java -classpath $CLASSES:${CLASSPATH} $MAIN
