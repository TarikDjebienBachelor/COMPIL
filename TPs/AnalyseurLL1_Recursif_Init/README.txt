README : 

// Auteur : Djebien Tarik
// Date   : octobre 2010
// Objet  : Analyseur LL(1) récursif de INIT

Ci-Joint le TP numero 3 de Compilation, tout est fonctionnel :

Arborescence de l'archive Tarik_Djebien_GROUPE2.tar.gz :
    |
    |_____README
    |
    |
    |____
           |
           |
           |________classes/
           |                     |__init/
           |                             |__executeurs/LanceurAnalyseurLexical.class LanceurAnalyseurSyntaxique.class
           |                             |__analyseurs/CUP$ParserInit$actions.class ParserInit.class ScannerInit.class ParserException.class
	   |                             |                     ScannerException.class  Symbole.class  TypeSymboles.class
           |                             |__testeurs/TesteurNegatifAnalyseurLexical.class TesteurPositifAnalyseurLexical.class
           |                                               TesteurNegatifAnalyseurSyntaxique.class TesteurPositifAnalyseurSyntaxique.class
           |__doc/ toute la JAVADOC
           |                     
           |__scripts/execEnLigneAnalyseurLexical.sh execSurFichierAnalyseurLexical.sh execTestsAnalyseurLexical.sh
           |              execEnLigneAnalyseurSyntaxique.sh execSurFichierAnalyseurSyntaxique.sh execTestsAnalyseurSyntaxique.sh
           |                     
           |__spec/ anLexInit.lex anSyntInit.cup
           |                     
           |__src/
           |      |___________init/
           |                             |__executeurs/LanceurAnalyseurLexical.java LanceurAnalyseurSyntaxique.java
           |                             |__analyseurs/ParserInit.java ScannerInit.java ParserException.java
	   |                             |                     ScannerException.java  Symbole.java  TypeSymboles.java
           |                             |__testeurs/TesteurNegatifAnalyseurLexical.java TesteurPositifAnalyseurLexical.java
           |                                               TesteurNegatifAnalyseurSyntaxique.java TesteurPositifAnalyseurSyntaxique.java
           |
           |_________________test/
                                            |__OK/  progliste.init ( un programme INIT accepté par l'analyseur lexical et syntaxique)
                                            |  
                                            |__KO/ 
                                                        proglisteError.ava ( un programme INIT accepté par l'analyseur lexical et refusé par le syntaxique )

Pour les commentaires :

tarik.djebien@etudiant.univ-lille1.fr

Cordialement.
