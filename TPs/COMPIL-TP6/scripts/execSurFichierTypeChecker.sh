#!/bin/bash

# ex�cution du contr�le de type Ava
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
MAIN=ava.executeurs.LanceurTypeChecker
#*******************************
java -classpath $CLASSES:${CLASSPATH} $MAIN $1
