/*****************************************************
 * Analyseur lexical pour Ava version simple
 * fichier de description pour JFlex
 * produit anLexAva/ScannerAva.java
 * F. Bellano
 * 07/10/10
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
// une macro peut être utilisée pour en définir une autre (de
// manière non récursive !) : il faut alors entourer son
// identificateur d'accolades.

finLigne = \r|\n|\r\n // convention Java
// | est le choix des expr reg de JFlex
// \n = retour-chariot sous Unix, \r = rc sous Windows

blancs = {finLigne} | [ \t\f] 
// \t = tabulation, \f = form-feed
// [ \t\f] est une classe de caractères qui dénote soit " ", soit \t,
// soit \f  

prog = program // une simple chaîne

identificateur = [:jletter:] [:jletterdigit:]* 
// * est l'étoile des expressions régulières standard
// [:jletter:] représente n'importe quel caractère qui peut débuter un
// identificateur Java 
// [:jletterdigit:] représente n'importe quel caractère qui peut
// suivre le 1er caractère d'un identificateur Java (donc une lettre
// ou un chiffre)    

entier = [:digit:]+
// x+ signifie classiquement xx*
// [:digit:]  représente n'importe quel chiffre
 
affect = := 

// commentaires débutant par -- et se finit par un saut de ligne
commentaires = -- [^\n\r]* {finLigne}

chaines = \"([^\n\r\"]|\\\")* \"

endloop = "end" ({blancs}|{finLigne})+ "loop"
endif = "end" ({blancs}|{finLigne})+ "if"

%%

/***********************************************************************/
/* Troisième partie : règles lexicales et actions. */
/***********************************************************************/

// syntaxe : { <nom_macro> | <expr_reg> } { <code_java> } un return ds
// le code Java correspond au retour d'un symbole (ici de type Symbol)
// résultat de la méthode d'analyse (ici la fonction next_token). 
// S'il n'y a pas de return, on passe au symbole suivant.

{blancs} { /* on ignore les blancs */ }

{prog} { // on a reconnu le mot-clé program
  return creerSymbole("PROG",TypeSymboles.PROG);
}

"int" { // on a reconnu le mot-clé int
  return creerSymbole("DECLINT",TypeSymboles.DECLINT);
}
"boolean" { // on a reconnu le mot-clé boolean
  return creerSymbole("DECLBOOL",TypeSymboles.DECLBOOL);
}
"write" { // on a reconnu le mot-clé write 
  return creerSymbole("WRITE",TypeSymboles.WRITE);
}
"writeln" { // on a recconu le mot-clé writeln
  return creerSymbole("WRITELN",TypeSymboles.WRITELN);
}
"read" {// on a reconnu un read
  return creerSymbole("READ",TypeSymboles.READ);
}
"if" { // on a reconnu le début d'une boucle if
  return creerSymbole("IF",TypeSymboles.IF);
}
"then" { // on a reconnu le mot-clé "then"
  return creerSymbole("THEN",TypeSymboles.THEN);
}
"else" { // on a reconnu le mot-clé "else"
  return creerSymbole("ELSE",TypeSymboles.ELSE);
}
{endif} { // on a reconnu le mot-clé "end if"
  return creerSymbole("ENDIF",TypeSymboles.ENDIF);
}
"while" { // on a reconnu le mot-clé "while"
  return creerSymbole("WHILE",TypeSymboles.WHILE);
}
"not" { // on a reconnu le mot-clé "not"
  return creerSymbole("NOT",TypeSymboles.NOT);
}
"loop" { // on a reconnu le mot-clé "loop"
  return creerSymbole("LOOP",TypeSymboles.LOOP);
}
{endloop} { // on a reconnu le mot-clé "end loop" 
  return creerSymbole("ENDLOOP",TypeSymboles.ENDLOOP);
}
"and" { // on a reconnu le mot-clé "and"
   return creerSymbole("AND",TypeSymboles.AND); 
}
"or" { // on a reconnu le mot-clé "or"
   return creerSymbole("OR",TypeSymboles.OR); 
}

"true" {  // on a reconnu le mot-clé "true"
   return creerSymbole("TRUE",TypeSymboles.TRUE);
}
"false" { // on a reconnu le mot-clé "false"
   return creerSymbole("FALSE",TypeSymboles.FALSE);
}
"mod" { // on a reconnu le mot-clé mod
  return creerSymbole("MOD",TypeSymboles.MOD);
}
// on a défini tous les mot-clés qui pourraient préfixer un indentificateur :
// on définit donc maintenant seulement les identificateurs

{identificateur} {// on a reconnu un identificateur, par la suite il
		  // faudra lui associer par ex son nom : on utilise
		  // yytext() qui représente la portion du texte
		  // d'entrée reconnue 
 return creerSymbole("IDENT",TypeSymboles.IDENT,yytext());
}
{entier} {// on a reconnu un entier, par la suite il faudra lui
	  // associer par ex sa valeur
  //return creerSymbole(TypeSymboles.ENTIER);
  return creerSymbole("ENTIER",TypeSymboles.ENTIER, new Integer(yytext()));
}
{affect} {// on a reconnu un opérateur d'affectation
  return creerSymbole("AFF",TypeSymboles.AFF);
}
";" { // on a reconnu un ";"
  return creerSymbole("FININSTR",TypeSymboles.FININSTR);
}
"," { // on a reconnu une ","
  return creerSymbole("SEPVAR",TypeSymboles.SEPVAR);
}
"=" { // on a recconu un "="
  return creerSymbole("EGAL",TypeSymboles.EGAL);
}
"/=" { // on a reconnu un "/="
  return creerSymbole("DIFF",TypeSymboles.DIFF);
}
"<=" { // on a reconnu un "<="
  return creerSymbole("INFE",TypeSymboles.INFE);
}
"<" { // on a reconnu un "<"
  return creerSymbole("INFS",TypeSymboles.INFS);
}
">=" { // on a reconnu un ">="
  return creerSymbole("SUPE",TypeSymboles.SUPE);
}
">" { // on a reconnu un ">"
  return creerSymbole("SUPS",TypeSymboles.SUPS);
}
"+" { // on a reconnu un "+"
  return creerSymbole("PLUS",TypeSymboles.PLUS);
}
"-" { // on a reconnu un "-"
  return creerSymbole("MOINS",TypeSymboles.MOINS);
}
"/" { // on a reconnu un "/"
  return creerSymbole("DIV",TypeSymboles.DIV);
}
"*" { // on a reconnu un "*"
  return creerSymbole("MULT",TypeSymboles.MULT);
}


{commentaires} { //on a reconnu un commentaire mais on fait rien
}

{chaines} { // on a reconnu une chaine de caractere
  return creerSymbole("CHAINE",TypeSymboles.CHAINE,yytext().substring(1,yylength() - 1));
}

"(" { // on a reconnu un "("
  return creerSymbole("PARENTO",TypeSymboles.PARENTO);
}
")" { // on a reconnu un ")"
  return creerSymbole("PARENTF",TypeSymboles.PARENTF);
}

"%i" { // on a reconnu un format d'impression d'entier
return creerSymbole("FORIMPINT",TypeSymboles.FORIMPINT);
}
"%b" { // on a reconnu un format d'impression de booleans
return creerSymbole("FORIMPBOOL",TypeSymboles.FORIMPBOOL);
}
"%s" { // on a reconnu un format d'impression de chaine de caracteres
return creerSymbole("FORIMPSTR",TypeSymboles.FORIMPSTR);
}



.|\n {// erreur : .|\n désigne n'importe quel caractère non reconnu
      // par une des règles précédentes 
  throw new ScannerException("symbole inconnu, caractère " + yytext() + 
				 " ligne " + yyline + " colonne " + yycolumn);
}  
