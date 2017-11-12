/*****************************************************
 * Analyseur lexical pour Ava v2
 * fichier de description pour JFlex
 * produit ava/analyseurs/ScannerAva.java
 * D. Tarik
 * 10/11/2010
 **********
******************************************/

/***********************************************************************
 * Premi�re partie : code utilisateur                                  
 **********************************************************************/   

// d�claration du paquetage auquel appartient la classe g�n�r�e 
package ava.analyseurs;       

%%

/***********************************************************************
 * Seconde partie : options et d�claration de macros
 **********************************************************************/

/*************************   Options  **********************/ 

// ATTENTION : le % doit toujours �tre en 1�re colonne

// la classe g�n�r�e implantant l'analyseur s'appelle ScannerAva.java  
%class ScannerAva
 // et est publique
%public
 // la classe g�n�r�e implante l'interface java_cup.runtime.Scanner
%implements java_cup.runtime.Scanner
 // pour utiliser les caract�res unicode
%unicode
 // pour garder trace du num�ro de ligne du caract�re trait�
%line
 // pour garder trace du num�ro de colonne du caract�re trait�
%column
 // l'analyseur fournit des symboles de type Symbol
%type java_cup.runtime.Symbol
 // la fonction de l'analyseur fournissant le prochain Symbol s'appelle
 // next_token...
%function next_token
 // ... et l�ve une exception ScannerException en cas d'erreur
%yylexthrow{
ScannerException
%yylexthrow}
 // action effectu�e qd la fin du fichier � analyser est rencontr�e
%eofval{
  return new Symbole(TypeSymboles.EOF);
%eofval}
// code inclus dans la classe (utile pour la d�claration
// d'attributs, de m�thodes...)
%{
  /** Cr�e un symbole de type t et de repr�sentation textuelle s, en
   * positionnant le num�ro de ligne et de colonne de ce symbole dans
   * le fichier source.
   * @param s la repr�sentation textuelle de ce symbole
   * @param t le type de ce symbole
   */
  private Symbole creerSymbole(String s, int t) {
    return new Symbole(s,t,yyline,yycolumn);
  }

  /** Cr�e un symbole de type t, de repr�sentation textuelle s, et de
   * valeur v en positionnant le num�ro de ligne et de colonne de ce
   * symbole dans le fichier source.  
   * @param s la repr�sentation textuelle de ce symbole
   * @param t le type de ce symbole
   * @param v la valeur de ce symbole
   */
  private Symbole creerSymbole(String s, int t, Object v) {
    return new Symbole(s,t,v,yyline,yycolumn);
  }  
%}

/*************************   definitions macros  **********************/ 

// syntaxe : <nom_macro> = <expr_reg>
// une macro peut �tre utilis�e pour en d�finir une autre (de
// mani�re non r�cursive !)

finLigne = \r|\n|\r\n
blancs = {finLigne} | [ \t\f]
interieurChaine = [^\n\"]
chaine = \" ({interieurChaine} | \\\" )* \"
commentaire = "--" [^\n\r]* {finLigne}
prog = "program"
int = "int" 
bool = "boolean" 
identificateur = [:jletter:] [:jletterdigit:]*
entier = [:digit:]+
plus = "+"
moins = "-"
div = "/"
mult = "*"
modulo = "mod"
affect = ":="
ptvirg = ";"
parOuvrante = "("
parFermante = ")"
if = "if"
fi = "end" {blancs}* "if"
then = "then"
else = "else"
while = "while"
loop = "loop"
done = "end" {blancs}* "loop"
virg = ","
read = "read"
write = "write"
alaligne = "writeln"
formatInt = "%i"
formatBool = "%b"
formatChaine = "%s"
egal = "="
diff = "/="
inf = "<"
sup = ">"
infegal = "<="
supegal = ">="
and = "and"
or = "or"
not = "not"

%%

/***********************************************************************/
/* Troisi�me partie : r�gles lexicales et actions. */
/***********************************************************************/

// syntaxe :
// { <nom_macro> | <expr_reg> } { <code_java> }
// un return ds le code Java correspond au retour d'un symbole
// r�sultat de la fonction next_token
// s'il n'y a pas de return, on passe au symbole suivant

{blancs} { /* on ignore les blancs */ }

{commentaire} { /* on ignore les commentaires */ }

{prog} { // on a reconnu le mot-cl� program
  return creerSymbole("PROG",TypeSymboles.PROG);
}

true {
  return creerSymbole("TRUE",TypeSymboles.TRUE);
}

false {
  return creerSymbole("FALSE",TypeSymboles.FALSE);
}


{int} { // on a reconnu le mot-cl� int
  return creerSymbole("DECLINT",TypeSymboles.DECLINT);
}



{bool} { // on a reconnu le mot-cl� boolean
  return creerSymbole("DECLBOOL",TypeSymboles.DECLBOOL);
}


{fi} {// on a reconnu un end if
  return creerSymbole("FI",TypeSymboles.FI);
}


{if} {// on a reconnu un if
  return creerSymbole("IF",TypeSymboles.IF);
}


{else} {// on a reconnu un else
  return creerSymbole("ELSE",TypeSymboles.ELSE);
}


{then} {// on a reconnu un then
  return creerSymbole("THEN",TypeSymboles.THEN);
}


{done} {// on a reconnu un end loop
  return creerSymbole("DONE",TypeSymboles.DONE);
}

{loop} {// on a reconnu un loop
  return creerSymbole("DO",TypeSymboles.LOOP);
}

{while} {// on a reconnu un while
  return creerSymbole("WHILE",TypeSymboles.WHILE);
}

{read} {// on a reconnu un read
  return creerSymbole("READ",TypeSymboles.READ);
}

{write} {// on a reconnu un write
  return creerSymbole("WRITE",TypeSymboles.WRITE);
}

{alaligne} {// on a reconnu un writeln
  return creerSymbole("WRITELN",TypeSymboles.WRITELN);
}

{chaine} {// on a reconnu une chaine
  return creerSymbole("CHAINE",TypeSymboles.CHAINE,yytext().substring(1,yylength() - 1));
}

{and} {// on a reconnu un and
  return creerSymbole("AND",TypeSymboles.AND);
}

{or} {// on a reconnu un or
  return creerSymbole("OR",TypeSymboles.OR);
}

{not} {// on a reconnu un not
  return creerSymbole("NOT",TypeSymboles.NOT);
}

{modulo} {// on a reconnu un op�rateur MOD
  return creerSymbole("MOD",TypeSymboles.MOD);
}

// tous les token ressemblant � des idents ont �t� trait�s

{identificateur} {// on a reconnu un identificateur
  return creerSymbole("IDENT",TypeSymboles.IDENT,yytext());
}

{formatInt} {// on a reconnu un format pour impression d'entier
  return creerSymbole("FORMATI",TypeSymboles.FORMATI);
}

{formatBool} {// on a reconnu un format pour impression de booleen
  return creerSymbole("FORMATB",TypeSymboles.FORMATB);
}

{formatChaine} {// on a reconnu un format pour impression de chaine
  return creerSymbole("FORMATC",TypeSymboles.FORMATC);
}


{egal} {// on a reconnu un comparateur pour �galit�
  return creerSymbole("EGAL",TypeSymboles.EGAL);
}

{diff} {// on a reconnu un comparateur pour diff�rence
  return creerSymbole("DIFF",TypeSymboles.DIFF);
}

{inf} {// on a reconnu un comparateur pour inf�rieur strict
  return creerSymbole("INF",TypeSymboles.INF);
}

{infegal} {// on a reconnu un comparateur pour inf�rieur ou �gal
  return creerSymbole("INFEGAL",TypeSymboles.INFEGAL);
}


{sup} {// on a reconnu un comparateur pour sup�rieur strict
  return creerSymbole("SUP",TypeSymboles.SUP);
}

{supegal} {// on a reconnu un comparateur pour sup�rieur ou �gal
  return creerSymbole("SUPEGAL",TypeSymboles.SUPEGAL);
}

{entier} {// on a reconnu un entier
  return creerSymbole("ENTIER",TypeSymboles.ENTIER,new Integer(yytext()));
}

{plus} {// on a reconnu un op�rateur plus
  return creerSymbole("PLUS",TypeSymboles.PLUS);
}

{moins} {// on a reconnu un op�rateur moins
  return creerSymbole("MOINS",TypeSymboles.MOINS);
}

{div} {// on a reconnu un op�rateur DIV
  return creerSymbole("DIV",TypeSymboles.DIV);
}

{mult} {// on a reconnu un op�rateur MULT
  return creerSymbole("MULT",TypeSymboles.MULT);
}



{affect} {// on a reconnu un op�rateur d'affectation
  return creerSymbole("AFFECT",TypeSymboles.AFFECT);
}

{ptvirg} {// on a reconnu un ";"
  return creerSymbole("FININSTR",TypeSymboles.FININSTR);
}

{virg} {// on a reconnu un ","
  return creerSymbole("SEP",TypeSymboles.SEP);
}


{parOuvrante} {// on a reconnu un "("
  return creerSymbole("PARO",TypeSymboles.PARO);
}

{parFermante} {// on a reconnu un ")"
  return creerSymbole("PARF",TypeSymboles.PARF);
}


.|\n {// erreur
  throw new ScannerException("token inconnu, caract�re " + yytext() + 
				 " ligne " + yyline + " colonne " + yycolumn);
}  
