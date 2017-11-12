README : 

// Auteur : Djebien Tarik
// Date   : octobre 2010
// Objet  : Analyseur lexicale et syntaxique de AVA

Ci-Joint le TP numero 2 de Compilation, tout est fonctionnel :

Arborescence de l'archive Tarik_Djebien_GROUPE2.tar.gz :
    |
    |_____README
    |
    |_____Le_Langage_AVA.pdf ( un resumé detaillé du langage AVA pour la construction de l'analyseur lexical).
    |
    |_____ava/
           |
           |
           |________classes/
           |                     |__ava/
           |                             |__executeurs/LanceurAnalyseurLexical.class
           |                             |__analyseurs/CUP$ParserAva$actions.class ParserAva.class ScannerAva.class ParserException.class
	   |                             |                     ScannerException.class  Symbole.class  TypeSymboles.class
           |                             |__testeurs/TesteurNegatifAnalyseurLexical.class TesteurPositifAnalyseurLexical.class
           |                                               TesteurNegatifAnalyseurSyntaxique.class TesteurPositifAnalyseurSyntaxique.class
           |__doc/ toute la JAVADOC
           |                     
           |__scripts/execEnLigneAnalyseurLexical.sh execSurFichierAnalyseurLexical.sh execTestsAnalyseurLexical.sh
           |              execEnLigneAnalyseurSyntaxique.sh execSurFichierAnalyseurSyntaxique.sh execTestsAnalyseurSyntaxique.sh
           |                     
           |__spec/ anLexAva.lex anSyntAva.cup
           |                     
           |__src/
           |      |___________ava/
           |                             |__executeurs/LanceurAnalyseurLexical.java LanceurAnalyseurSyntaxique.java
           |                             |__analyseurs/ParserAva.java ScannerAva.java ParserException.java
	   |                             |                     ScannerException.java  Symbole.java  TypeSymboles.java
           |                             |__testeurs/TesteurNegatifAnalyseurLexical.java TesteurPositifAnalyseurLexical.java
           |                                               TesteurNegatifAnalyseurSyntaxique.java TesteurPositifAnalyseurSyntaxique.java
           |
           |_________________test/
                                            |__OK/  Factorielle.ava ( un programme AVA accepté par l'analyseur lexical et syntaxique)
                                            |  
                                            |__KO/ Failed.ava ( un programme AVA refusé par l'analyseur lexical donc syntaxique)
                                                        erreur.ava ( un programme AVA accepté par l'analyseur lexical et refusé par le syntaxique )

Pour les commentaires :

tarik.djebien@etudiant.univ-lille1.fr

Cordialement.
