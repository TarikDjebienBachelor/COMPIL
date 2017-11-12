package init.analyseurs;

import java.io.*;

/** Un analyseur syntaxique LL1 pour Init étendu avec le type liste.
 *
 * @author D. Tarik
 * @version octobre 2010
 */
public class ParserLL1Init {

    private ScannerInit anLex;
    private Symbole teteLecture;

    public ParserLL1Init (ScannerInit anLex) {
	this.anLex = anLex;
    }

    /** Vérifie que l'unité lexicale du symbole courant (celui "sous
     * la tête de lecture") coïncide avec l'unité lexicale passée en
     * paramètre. Si c'est le cas un nouveau symbole est lu et stocké
     * ds le symbole courant, sinon une exception est levée.
     * @param type le type du symbole attendu
     * @throws ParserException si ul et l'unité lexicale du symbole
     * courant ne coïncident pas
     * @throws ScannerException en cas d'erreur lexicale
     * @throws IOException en cas d'erreur d'E/S dans next_token()
     * (imposé par JFlex)
     * @throws Exception éventuellement dans next_token() (imposé par
     * l'interface Scanner de Cup)
     */
    private void consommer(int type)
	throws Exception, ScannerException, java.io.IOException, ParserException
    {
	if (this.teteLecture.getType() == type)
	    this.teteLecture = this.prochainSymb();
	else throw new ParserException("erreur consommation "
				       + this.dumpTeteLecture());
    }

    /** Retourne le prochain symbole délivré par l'analyseur lexical.
     * @return le prochain symbole, demandé à l'analyseur lexical
     * @throws Exception éventuellement dans next_token() (imposé par
     * l'interface Scanner de Cup)
     */
    private Symbole prochainSymb() throws Exception {
	return (Symbole) this.anLex.next_token();
    }

    /** Retourne un message d'erreur explicitant le symbole courant.
     * @return une chaîne représentant le symbole courant qui a causé
     * une erreur.
     */
    private String dumpTeteLecture() {
	return "symbole " + this.teteLecture.getRepresentationTextuelle() + " ligne "
	    + (this.teteLecture.left + 1) + " colonne " + (this.teteLecture.right + 1);
    }

    /** Lance cet analyseur syntaxique (donc consomme le premier
     * symbole), termine normalement si l'expression à analyser est un
     * programme Init sémantiquement correct, lève une exception
     * sinon.
     * @throws ParserException si l'expression à analyser n'est
     * syntaxiquement pas un programme Init
     * @throws ScannerException en cas d'erreur lexicale
     * @throws IOException en cas d'erreur d'E/S dans next_token()
     * (imposé par JFlex)
     * @throws Exception éventuellement dans next_token() (imposé par
     * l'interface Scanner de Cup)
     */
    public void parse()
	throws Exception, ParserException, ScannerException, IOException
    {
	this.teteLecture = this.prochainSymb();
	/******************************************************************
              CHANGER AU FUR ET À MESURE ICI LE NON-TERMINAL À RECONNAÎTRE
	******************************************************************/
	this.programme();
	if (this.teteLecture.getType() != TypeSymboles.EOF)
	    throw new ParserException("erreur EOF " + this.dumpTeteLecture());
    }


    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal programme (axiome du programme), lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * un programme
     */
    private void programme()
	throws ParserException, ScannerException, IOException, Exception {
	// programme -> entete listeDecl listeInstr
	if (this.teteLecture.getType() == TypeSymboles.PROG) {
	    this.entete();
	    this.listeDecl();
	    this.listeInstr();
	}
	else throw new ParserException("erreur ds programme() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal entete (en-tête du programme), lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * un en-tête
     */
    private void entete()
	throws ParserException, ScannerException, IOException, Exception {
	// entete -> PROG IDENT FININSTR
	if (this.teteLecture.getType() == TypeSymboles.PROG) {
	    this.consommer(TypeSymboles.PROG);
	    this.consommer(TypeSymboles.IDENT);
	    this.consommer(TypeSymboles.FININSTR);
	}
	else throw new ParserException("erreur ds entete() " + this.dumpTeteLecture());
    }


    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal listeDecl, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une listeDecl
     */
    private void listeDecl()
	throws ParserException, ScannerException, IOException, Exception {
	// listeDecl ->  | decl listeDecl
	if (this.teteLecture.getType() == TypeSymboles.DECLINT
	 || this.teteLecture.getType() == TypeSymboles.DECLLIST)
	 {
	    this.decl();
	    this.listeDecl();
	 }
	else
	if (this.teteLecture.getType() == TypeSymboles.READ
	 || this.teteLecture.getType() == TypeSymboles.IDENT
	 || this.teteLecture.getType() == TypeSymboles.EOF)
	 {
		 // ne rien faire
     }
	else throw new ParserException("erreur ds listeDecl() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal decl, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une decl
     */
    private void decl()
	throws ParserException, ScannerException, IOException, Exception {
	// decl -> DECLINT listeIdent FININSTR | DECLLIST listeIdent FININSTR
	if (this.teteLecture.getType() == TypeSymboles.DECLINT)
	 {
		this.consommer(TypeSymboles.DECLINT);
	    this.listeIdent();
	    this.consommer(TypeSymboles.FININSTR);
	 }
	else
	if (this.teteLecture.getType() == TypeSymboles.DECLLIST)
	 {
		this.consommer(TypeSymboles.DECLLIST);
	    this.listeIdent();
	    this.consommer(TypeSymboles.FININSTR);
     }
	else throw new ParserException("erreur ds decl() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal listeIdent, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une listeIdent
     */
    private void listeIdent()
	throws ParserException, ScannerException, IOException, Exception {
	// listeIdent -> debListeIdent suiteListeIdent
	if (this.teteLecture.getType() == TypeSymboles.IDENT)
	 {
		this.debListeIdent();
	    this.suiteListeIdent();
	 }
	else throw new ParserException("erreur ds listeIdent() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal debListeIdent, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une debListeIdent
     */
    private void debListeIdent()
	throws ParserException, ScannerException, IOException, Exception {
	// debListeIdent -> IDENT
	if (this.teteLecture.getType() == TypeSymboles.IDENT)
	 {
		this.consommer(TypeSymboles.IDENT);
	 }
	else throw new ParserException("erreur ds debListeIdent() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal suiteListeIdent, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une suiteListeIdent
     */
    private void suiteListeIdent()
	throws ParserException, ScannerException, IOException, Exception {
	// suiteListeIdent ->  | SEPVAR listeIdent
	if (this.teteLecture.getType() == TypeSymboles.FININSTR)
	 {
		// ne rien faire
	 }
	else
	if (this.teteLecture.getType() == TypeSymboles.SEPVAR)
	 {
		 this.consommer(TypeSymboles.SEPVAR);
		 this.listeIdent();
	 }
	else throw new ParserException("erreur ds suiteListeIdent() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal listeInstr, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une listeInstr
     */
    private void listeInstr()
	throws ParserException, ScannerException, IOException, Exception {
	// listeInstr ->  | instr listeInstr
	if (this.teteLecture.getType() == TypeSymboles.READ
	 || this.teteLecture.getType() == TypeSymboles.IDENT)
	 {
		this.instr();
		this.listeInstr();
	 }
	else
	if (this.teteLecture.getType() == TypeSymboles.EOF)
	 {
		 // ne rien faire
	 }
	else throw new ParserException("erreur ds listeInstr() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal instr, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une instr
     */
    private void instr()
	throws ParserException, ScannerException, IOException, Exception {
	// instr -> affect | lect
	if (this.teteLecture.getType() == TypeSymboles.READ)
	 {
		this.lect();
	 }
	else
	if (this.teteLecture.getType() == TypeSymboles.IDENT)
	 {
		this.affect();
	 }
	else throw new ParserException("erreur ds instr() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal affect, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une affect
     */
    private void affect()
	throws ParserException, ScannerException, IOException, Exception {
	// affect -> IDENT AFF exprAffect FININSTR
	if (this.teteLecture.getType() == TypeSymboles.IDENT)
	 {
		this.consommer(TypeSymboles.IDENT);
		this.consommer(TypeSymboles.AFF);
		this.exprAffect();
		this.consommer(TypeSymboles.FININSTR);
	 }
	else throw new ParserException("erreur ds affect() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal lect, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une lect
     */
    private void lect()
	throws ParserException, ScannerException, IOException, Exception {
	// lect -> READ IDENT FININSTR
	if (this.teteLecture.getType() == TypeSymboles.READ)
	 {
		this.consommer(TypeSymboles.READ);
		this.consommer(TypeSymboles.IDENT);
		this.consommer(TypeSymboles.FININSTR);
	 }
	else throw new ParserException("erreur ds lect() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal exprAffect, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une exprAffect
     */
    private void exprAffect()
	throws ParserException, ScannerException, IOException, Exception {
	// exprAffect -> ENTIER | liste
	if (this.teteLecture.getType() == TypeSymboles.ENTIER)
	 {
		this.consommer(TypeSymboles.ENTIER);
	 }
	else
	if (this.teteLecture.getType() == TypeSymboles.DEBLISTE)
     {
	    this.liste();
     }
	else throw new ParserException("erreur ds exprAffect() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal liste, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une liste
     */
    private void liste()
	throws ParserException, ScannerException, IOException, Exception {
	// liste -> DEBLISTE listeElt FINLISTE
	if (this.teteLecture.getType() == TypeSymboles.DEBLISTE)
	 {
		this.consommer(TypeSymboles.DEBLISTE);
		this.listeElt();
		this.consommer(TypeSymboles.FINLISTE);
	 }
	else throw new ParserException("erreur ds liste() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal listeElt, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une listeElt
     */
    private void listeElt()
	throws ParserException, ScannerException, IOException, Exception {
	// listeElt -> | elt listeElt
	if (this.teteLecture.getType() == TypeSymboles.ENTIER
	 || this.teteLecture.getType() == TypeSymboles.DEBLISTE )
	 {
		this.elt();
		this.listeElt();
	 }
	else
	if (this.teteLecture.getType() == TypeSymboles.FINLISTE )
	{
	    // ne rien faire
	}
	else throw new ParserException("erreur ds listeElt() " + this.dumpTeteLecture());
    }

    /** Reconnaît dans l'expression d'entrée un fragment engendré par
     * le non-terminal elt, lève une
     * exception si ce n'est pas possible.
     * @throws ParserException si l'expression à analyser n'est pas
     * une elt
     */
    private void elt()
	throws ParserException, ScannerException, IOException, Exception {
	// elt -> ENTIER | liste
	if (this.teteLecture.getType() == TypeSymboles.ENTIER)
	 {
		this.consommer(TypeSymboles.ENTIER);
	 }
	else
	if (this.teteLecture.getType() == TypeSymboles.DEBLISTE )
	{
	    this.liste();
	}
	else throw new ParserException("erreur ds elt() " + this.dumpTeteLecture());
    }
}

