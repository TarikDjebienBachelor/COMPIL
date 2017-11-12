package ava.analyseurs;       

%%
 
%class ScannerAva
%public
%implements java_cup.runtime.Scanner
%unicode
%line
%column
%type java_cup.runtime.Symbol
%function next_token
%yylexthrow{
ScannerException
%yylexthrow}
%eofval{
  return new Symbole(TypeSymboles.EOF);
%eofval}
%{
private Symbole creerSymbole(String s, int t) {
    return new Symbole(s,t,yyline,yycolumn);
  }
private Symbole creerSymbole(String s, int t, Object v) {
    return new Symbole(s,t,v,yyline,yycolumn);
  }  
%}

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
fi = "end" {blancs}+ "if"
then = "then"
else = "else"
while = "while"
loop = "loop"
done = "end" {blancs}+ "loop"
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

{blancs} { /* on ignore les blancs */ }
{commentaire} { /* on ignore les commentaires */ }
{prog} { // on a reconnu le mot-clé program
  return creerSymbole("PROG",TypeSymboles.PROG);
}
true {
  return creerSymbole("TRUE",TypeSymboles.TRUE);
}
false {
  return creerSymbole("FALSE",TypeSymboles.FALSE);
}
{int} { // on a reconnu le mot-clé int
  return creerSymbole("DECLINT",TypeSymboles.DECLINT);
}
{bool} { // on a reconnu le mot-clé boolean
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
{modulo} {// on a reconnu un opérateur MOD
  return creerSymbole("MOD",TypeSymboles.MOD);
}
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
{egal} {// on a reconnu un comparateur pour égalité
  return creerSymbole("EGAL",TypeSymboles.EGAL);
}
{diff} {// on a reconnu un comparateur pour différence
  return creerSymbole("DIFF",TypeSymboles.DIFF);
}
{inf} {// on a reconnu un comparateur pour inférieur strict
  return creerSymbole("INF",TypeSymboles.INF);
}
{infegal} {// on a reconnu un comparateur pour inférieur ou égal
  return creerSymbole("INFEGAL",TypeSymboles.INFEGAL);
}
{sup} {// on a reconnu un comparateur pour supérieur strict
  return creerSymbole("SUP",TypeSymboles.SUP);
}
{supegal} {// on a reconnu un comparateur pour supérieur ou égal
  return creerSymbole("SUPEGAL",TypeSymboles.SUPEGAL);
}
{entier} {// on a reconnu un entier
  return creerSymbole("ENTIER",TypeSymboles.ENTIER,new Integer(yytext()));
}
{plus} {// on a reconnu un opérateur plus
  return creerSymbole("PLUS",TypeSymboles.PLUS);
}
{moins} {// on a reconnu un opérateur moins
  return creerSymbole("MOINS",TypeSymboles.MOINS);
}
{div} {// on a reconnu un opérateur DIV
  return creerSymbole("DIV",TypeSymboles.DIV);
}
{mult} {// on a reconnu un opérateur MULT
  return creerSymbole("MULT",TypeSymboles.MULT);
}
{affect} {// on a reconnu un opérateur d'affectation
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
  throw new ScannerException("token inconnu, caractère " + yytext() + 
				 " ligne " + yyline + " colonne " + yycolumn);
}  
