#!/bin/sh

# ex�cution du contr�le de type Ava
# texte � analyser en ligne
#
# M. Nebut - 16/12/04


#*******************************
# le r�pertoire de r�f�rence
REP=.
# le r�pertoire contenant vos .class
CLASSES=$REP/classes
# la classe contenant votre main
MAIN=ava.executeurs.LanceurTypeChecker

#*******************************

echo "Entrez le texte � analyser :"
java -classpath $CLASSES:${CLASSPATH} $MAIN
