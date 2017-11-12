#!/bin/sh

# test contrôle de type Ava
#
# -ea : activation à l'exécution des assert
#
# M. Nebut - 16/12/04

#*******************************
# le répertoire de référence
REP=.
# le répertoire contenant vos .class
CLASSES=$REP/classes
# la classe contenant votre main pour les tests positifs
MAINPOS=ava.testeurs.TesteurPositifTypeChecker
# la classe contenant votre main pour les tests négatifs
MAINNEG=ava.testeurs.TesteurNegatifTypeChecker
# répertoire test positif
POS=test/OK
# répertoire test negatif
NEG=test/KO

#*******************************



echo "*******************************************" ;
echo "***                 OK                  ***" ;
echo "*******************************************" ;

for f in `find $POS -name \*.ava` 
do
    echo $f ;
    java -ea -classpath $CLASSES:${CLASSPATH} $MAINPOS $f ;
    echo "";
done



echo "*******************************************" ;
echo "***                 KO                  ***" ;
echo "*******************************************" ;

for f in `find $NEG -name \*.ava`
do
    echo $f ;
    java -ea -classpath $CLASSES:${CLASSPATH} $MAINNEG $f ;
    echo "";
done

