#!/bin/bash

# ex�cution du g�n�rateur de code Ava
# texte � analyser ds un fichier
# nom du fichier en ligne de commande
#
# M. Nebut - 16/12/04

#*******************************
# le r�pertoire de r�f�rence
REP=.
# le r�pertoire contenant vos .class
CLASSES=$REP/classes
# la classe contenant votre main
MAIN=ava.executeurs.LanceurGenerateurCode
#*******************************
java -classpath $CLASSES:${CLASSPATH} $MAIN $1
