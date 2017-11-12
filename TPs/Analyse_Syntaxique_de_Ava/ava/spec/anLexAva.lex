/*****************************************************
 * Analyseur lexical pour Ava
 * fichier de description pour JFlex
 * produit anLexAva/ScannerAva.java
 * Djebien Tarik
 * 20/09/2010
 ****************************************************/

/***********************************************************************
 * Première partie : code utilisateur inclus tel quel dans le fichier
 * .java généré. On met typiquement ici les déclarations de paquetage
 * (package) et les importations de classes (import).
 ***********************************************************************/

// déclaration du paquetage auquel appartient la classe générée
package ava.analyseurs;

%%

/***********************************************************************
 * Seconde partie : options et déclarations de macros. 
 ***********************************************************************/

/*************************   Options  **********************/ 

// ATTENTION : le % doit toujours être en 1ère colonne

// la classe générée implantant l'analyseur s'appelle ScannerAva.java
%class ScannerAva
// et est publique
%public
// la cl. générée implante l'itf java_cup.runtime.Scanner fournie par Cup
%implements java_cup.runtime.Scanner
// pour utiliser les caractères unicode
%unicode
// pour garder trace du numéro de ligne du caractère traité
%line
// pour garder trace du numéro de colonne du caractère traité
%column
// l'an. lex. retourne des symboles de type java_cup.runtime.Symbol
%type java_cup.runtime.Symbol
// la fonction de l'analyseur fournissant le prochain Symbol s'appelle
// next_token...
%function next_token
// ... et lève une exception ScannerException en cas d'erreur lexicale
%yylexthrow{
ScannerException
%yylexthrow}
// action effectuée qd la fin du fichier à analyser est rencontrée
// le type EOF est généré automatiquement par Cup
%eofval{
  return new Symbole(TypeSymboles.EOF);
%eofval}
// code recopié dans la classe générée
%{
  private Symbole creerSymbole(String representation, int type) {
    return new Symbole(representation,type,yyline,yycolumn);
  }

  private Symbole creerSymbole(String representation, int type, Object valeur) {
    return new Symbole(representation,type,valeur,yyline,yycolumn);
  }  
%}

/*************************   définitions macros  **********************/ 

// une macro est une abbréviation pour une expression régulière

// syntaxe : <nom_macro> = <expr_reg>

finLigne = \r|\n|\r\n // convention Java
// | est le choix des expr reg de JFlex
// \n = retour-chariot sous Unix, \r = rc sous Windows

blancs = {finLigne} | [ \t\f] 
// \t = tabulation, \f = form-feed
// [ \t\f] est une classe de caractères qui dénote soit " ", soit \t,
// soit \f  

prog = program 
typeEntier = int
typeBoolean = boolean

identificateur = [:jletter:] [:jletterdigit:]* 
// [:jletter:] représente n'importe quel caractère qui peut débuter un
// identificateur Ava 
// [:jletterdigit:] représente n'importe quel caractère qui peut
// suivre le 1er caractère d'un identificateur Ava (donc une lettre
// ou un chiffre)    

entier = [:digit:]+
// [:digit:]  représente n'importe quel chiffre

VI = ","
PV = ";"
affect = ":=" 
debutComm = "--"
Commentaire = {debutComm}[^\n\r]*{finLigne}
guillemet = \"

parentheseO = "("
parentheseF = ")"

OperateurMoins = "-"
OperateurMod = "mod" | "MOD"
OperateurMult = "*"
OperateurDiv = "/"
OperateurPlus = "+"

egal = "="
different = "/="
infegal = "<="
inf =  "<"
sup = ">"
supegal = ">="

OperateurNot = "not" | "NOT"
OperateurAnd = "and" | "AND"
OperateurOr = "or" | "OR"
Vrai = "true" | "TRUE"
Faux = "false" | "FALSE"

endif = end{blancs}*if
endloop = end{blancs}*loop
formatEntier = %i
formatBooleen = %b
formatChaine = %s
echappement = \\
chaine = {guillemet}  [^\n\r\"]* ({echappement}{guillemet})* {guillemet}

%%

/***********************************************************************/
/* Troisième partie : règles lexicales et actions. */
/***********************************************************************/

// syntaxe : { <nom_macro> | <expr_reg> } { <code_java> }

{blancs} { /* on ignore les blancs */ }
{prog} { // on a reconnu le mot-clé program
  return creerSymbole("PROG",TypeSymboles.PROG);
}
{typeEntier} { // on a reconnu le mot-clé int
  return creerSymbole("DECLINT",TypeSymboles.DECLINT);
}
{typeBoolean} { // on a reconnu le mot-clé boolean
  return creerSymbole("DECLBOOL",TypeSymboles.DECLBOOL);
}
write {// on a reconnu un write
  return creerSymbole("WRITE",TypeSymboles.WRITE);
}
writeln {// on a reconnu un writeln
  return creerSymbole("WRITELN",TypeSymboles.WRITELN);
}
read {// on a reconnu un read
  return creerSymbole("READ",TypeSymboles.READ);
}

if {// on a reconnu un if
  return creerSymbole("IF",TypeSymboles.IF);
}

then {// on a reconnu un then
  return creerSymbole("THEN",TypeSymboles.THEN);
}

else {// on a reconnu un else
  return creerSymbole("ELSE",TypeSymboles.ELSE);
}

{endif} {// on a reconnu un end if
  return creerSymbole("ENDIF",TypeSymboles.ENDIF);
}

while {// on a reconnu un while
  return creerSymbole("WHILE",TypeSymboles.WHILE);
}

loop {// on a reconnu un loop
  return creerSymbole("LOOP",TypeSymboles.LOOP);
}

{endloop} {// on a reconnu un endloop
  return creerSymbole("ENDLOOP",TypeSymboles.ENDLOOP);
}

{OperateurMod} {// on a reconnu un mod
  return creerSymbole("MODULO",TypeSymboles.MODULO);
}

{OperateurNot} {// on a reconnu un "not"
  return creerSymbole("NOT",TypeSymboles.NOT);
}

{OperateurAnd} {// on a reconnu un and
  return creerSymbole("AND",TypeSymboles.AND);
}

{OperateurOr} {// on a reconnu un or
  return creerSymbole("OR",TypeSymboles.OR);
}

{Vrai} {// on a reconnu un TRUE
  return creerSymbole("VRAI",TypeSymboles.VRAI);
}

{Faux} {// on a reconnu un FALSE
  return creerSymbole("FAUX",TypeSymboles.FAUX);
}

// on a défini tous les mot-clés 

{identificateur} {// on a reconnu un identificateur
  return creerSymbole("IDENT",TypeSymboles.IDENT,yytext());
}

{entier} {// on a reconnu un entier
  return creerSymbole("ENTIER",TypeSymboles.ENTIER, new Integer(yytext()));
}
{affect} {// on a reconnu un opérateur d'affectation
  return creerSymbole("AFF",TypeSymboles.AFF);
}

{PV} {// on a reconnu un ";"
  return creerSymbole("FININSTR",TypeSymboles.FININSTR);
}

{VI} {// on a reconnu un ","
  return creerSymbole("SEPVAR",TypeSymboles.SEPVAR);
}

{parentheseO} {// on a reconnu un "("
  return creerSymbole("OPENPARENTH",TypeSymboles.OPENPARENTH);
}

{parentheseF} {// on a reconnu un ")"
  return creerSymbole("CLOSEPARENTH",TypeSymboles.CLOSEPARENTH);
}

{OperateurMoins} {// on a reconnu un "-"
  return creerSymbole("MOINS",TypeSymboles.MOINS);
}

{OperateurMult} {// on a reconnu un "*"
  return creerSymbole("MULT",TypeSymboles.MULT);
}

{OperateurDiv} {// on a reconnu un "/"
  return creerSymbole("DIV",TypeSymboles.DIV);
}

{OperateurPlus} {// on a reconnu un "+"
  return creerSymbole("PLUS",TypeSymboles.PLUS);
}

{egal} {// on a reconnu un "="
  return creerSymbole("EGAL",TypeSymboles.EGAL);
}

{different} {// on a reconnu un "/="
  return creerSymbole("DIFF",TypeSymboles.DIFF);
}

{infegal} {// on a reconnu un "<="
  return creerSymbole("INFEGAL",TypeSymboles.INFEGAL);
}

{inf} {// on a reconnu un "<"
  return creerSymbole("INF",TypeSymboles.INF);
}

{sup} {// on a reconnu un ">"
  return creerSymbole("SUP",TypeSymboles.SUP);
}

{supegal} {// on a reconnu un ">="
  return creerSymbole("SUPEGAL",TypeSymboles.SUPEGAL);
}


{Commentaire} { // on a reconnu un "- -commentaire"
  // et on ne fait rien comme dans tout langage.
}

{guillemet} { // on a reconnu un " " "
  return creerSymbole("GUILLEMET",TypeSymboles.GUILLEMET);
}

{chaine} { // on a reconnu un " " "
  return creerSymbole("STRING",TypeSymboles.STRING);
}

{formatEntier} { // on a reconnu un " %i "
  return creerSymbole("FORMATENTIER",TypeSymboles.FORMATENTIER);
}
{formatBooleen} { // on a reconnu un " %b "
  return creerSymbole("FORMATBOOLEEN",TypeSymboles.FORMATBOOLEEN);
}
{formatChaine} { // on a reconnu un " %s "
  return creerSymbole("FORMATCHAINE",TypeSymboles.FORMATCHAINE);
}
{echappement} { // on a reconnu un " \ "
  return creerSymbole("CANCEL",TypeSymboles.CANCEL);
}

.|\n {// erreur : .|\n désigne n'importe quel caractère non reconnu
      // par une des règles précédentes 
  throw new ScannerException("symbole inconnu, caractère " + yytext() + 
				 " ligne " + yyline + " colonne " + yycolumn);
}  
