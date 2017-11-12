#!/bin/sh

# exécution de LanceurDecompilateur
# texte à analyser ds un fichier
# nom du fichier passé en ligne de commande
#
# M. Nebut

#*******************************
# la classe contenant votre main
MAIN=ava.executeurs.LanceurDecompilateur

#*******************************
java $MAIN $1
