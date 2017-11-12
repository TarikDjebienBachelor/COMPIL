README : 

// Auteur : Djebien Tarik
// Date   : septembre 2010
// Objet  : Analyseur lexicale de AVA

Ci-Joint le TP numero 1 de Compilation, tout est fonctionnel :

Arborescence de l'archive Tarik_Djebien_GROUPE2.tar.gz :
    |
    |_____README
    |
    |_____Djebien_Tarik_AVA.pdf ( un resumé detaillé du langage AVA pour la construction de l'analyseur lexical).
    |
    |_____ava/
           |
           |
           |________classes/
           |                     |__ava/
           |                             |__executeurs/LanceurAnalyseurLexical.class
           |                             |__analyseurs/CUP$ParserAva$actions.class ParserAva.class ScannerAva.class
	   |                             |                     ScannerException.class  Symbole.class  TypeSymboles.class
           |                             |__testeurs/TesteurNegatifAnalyseurLexical.class TesteurPositifAnalyseurLexical.class
           |__doc/
           |                     
           |__scripts/execEnLigneAnalyseurLexical.sh execSurFichierAnalyseurLexical.sh execTestsAnalyseurLexical.sh
           |                     
           |__spec/ anLexAva.lex anSyntAva.cup
           |                     
           |__src/
           |      |___________ava/
           |                             |__executeurs/LanceurAnalyseurLexical.java
           |                             |__analyseurs/ParserAva.java ScannerAva.java
	   |                             |                     ScannerException.java  Symbole.java  TypeSymboles.java
           |                             |__testeurs/TesteurNegatifAnalyseurLexical.java TesteurPositifAnalyseurLexical.java
           |
           |_________________test/
                                            |__OK/  Factorielle.ava ( un programme AVA accepté par l'analyseur lexical)
                                            |  
                                            |__KO/ Failed.ava ( un programme AVA refusé par l'analyseur lexical )

Pour les commentaires :

tarik.djebien@etudiant.univ-lille1.fr

Cordialement.
