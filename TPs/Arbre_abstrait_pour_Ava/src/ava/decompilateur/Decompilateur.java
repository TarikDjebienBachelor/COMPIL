package ava.decompilateur;

import ava.arbreAbstrait.*;

import java.util.Iterator;

/** Un décompilateur pour Ava : fournit les méthodes pour produire une
 * représentation textuelle d'un arbre abstrait. Chaque méthode
 * visiteX complète un atttribut représentant la représentation
 * textuelle en construction. Chaque instruction a la responsabilité
 * du retour chariot qui débute sa représentation textuelle.
 * @author M. Nebut
 * @version 11/07
 */
public class Decompilateur implements Visiteur {

    /** Représentation textuelle courante. */
    private String repTextuelle;
    /** Programme à décompiler */
    private Programme programme;

    /** Construit un décompilateur pour l'arbre abstrait passé en
     * paramètre. 
     * @param p le programme à décompiler.
     */
    public Decompilateur(Programme p) {
	this.repTextuelle = "";
	this.programme = p;
    }


    /** Décompile l'arbre abstrait associé à ce décompilateur. 
     * @return une version textuelle de l'arbre abstrait associé à ce
     * décompilateur.  
     */ 
    public String decompile() 
	throws DecompilateurException, VisiteurException {	
	this.visiteProgramme(this.programme);
	return this.repTextuelle;
    }

    /** Décompile le programme passé en paramètre.
     * @param p le programme à décompiler 
     * @throws DecompilateurException si <tt>p</tt> vaut null
     */
    public void visiteProgramme(Programme p) 
	throws DecompilateurException, VisiteurException {
	if (p == null)
	    throw new DecompilateurException();

	this.repTextuelle = this.repTextuelle + "-- texte produit par decompilation\n";
	// entête
	this.repTextuelle = this.repTextuelle + "program \"" + p.getNom()
	    + "\";";

	// déclarations
	ListeDeclaration ldecl = p.getListeDecl();
	this.visiteListeDecl(ldecl);

	// instructions
	ListeInstruction linstr = p.getListeInstr();
	this.visiteListeInstr(linstr);
    }
    

    /********************* Déclarations ****************/

    /** Décompile la liste de déclaration passée en paramètre : chaque
     * identificateur déclaré est affiché avec son type.
     * @param ldecl la liste de déclarations 
     */
    public void visiteListeDecl(ListeDeclaration ldecl) 
	throws VisiteurException {
	Iterator it = ldecl.iterator();
	while (it.hasNext()) {
	    Declaration d = (Declaration) it.next();
	    d.visite(this);
	}
    }


    /** Décompile la déclaration de type entier passée en paramètre.
     * @param decl la déclaration de type entier à décompiler
     * @throws DecompilateurException si <tt>decl</tt> vaut null.
     */
    public void visiteDeclInt(DeclarationEntier decl) 
	throws DecompilateurException {
	if (decl == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" +  "int " + decl.getIdent() 
	    + ";";
    }


    /** Décompile la déclaration de type booléen passée en paramètre.
     * @param decl la déclaration de type booléen à décompiler
     * @throws DecompilateurException si <tt>decl</tt> vaut null.
     */
    public void visiteDeclBool(DeclarationBooleen decl) 
	throws DecompilateurException {
	if (decl == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" + "bool " + decl.getIdent() 
	    + ";";
    }

    /********************* Expressions *******************/

    /** Décompile l'entier passé en paramètre.
     * @param e l'entier à décompiler
     * @throws DecompilateurException si <tt>e</tt> vaut null.
     */
    public void visiteEntier(Entier e) 
	throws DecompilateurException {
	if (e == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + e.getValeur(); 
    }

    /** Décompile l'identificateur passé en paramètre.
     * @param id l'identificateur à décompiler
     * @throws DecompilateurException si <tt>id</tt> vaut null.
     */
    public void visiteIdentExpr(IdentExpr id) 
	throws DecompilateurException {
	if (id == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + id.getNom(); 
    }

    /** Décompile la constante passée en paramètre.
     * @param c la constante à décompiler
     * @throws DecompilateurException si <tt>c</tt> vaut null.
     */
    public void visiteExprTrue(ExprTrue c) 
	throws DecompilateurException {
	if (c == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "true";
    }

    /** Décompile la constante passée en paramètre.
     * @param c la constante à décompiler
     * @throws DecompilateurException si <tt>c</tt> vaut null.
     */
    public void visiteExprFalse(ExprFalse c) 
	throws DecompilateurException {
	if (c == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "false";
    }


    /** Décompile l'opérande gauche de l'expression binaire <tt>expr</tt>. 
     * @param expr l'expression dont on veut décompiler l'opérande gauche.     
     */
    private void decompExprGauche(ExpressionBinaire expr) 
	                          throws VisiteurException {
	this.repTextuelle = this.repTextuelle + "("; 
	// opérande gauche
	Expression gauche = expr.getExprGauche();
	gauche.visite(this);	
    }

    /** Décompile l'opérande droit de l'expression binaire <tt>expr</tt>. 
     * @param expr l'expression dont on veut décompiler l'opérande droit.     
     */
    private void decompExprDroite(ExpressionBinaire expr) 
	                          throws VisiteurException {
	// opérande droit
	Expression droite = expr.getExprDroite();
	droite.visite(this);
	this.repTextuelle = this.repTextuelle + ")";
    }

    /** Décompile l'addition passée en paramètre en l'entourant de
     * parenthèses.
     * @param expr l'addition à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteAddition(Addition expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.decompExprGauche(expr);
	this.repTextuelle = this.repTextuelle + " + ";
	this.decompExprDroite(expr);
    }


    /** Décompile la soustraction passée en paramètre en l'entourant de
     * parenthèses.
     * @param expr la soustraction à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteSoustraction(Soustraction expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.decompExprGauche(expr);
	this.repTextuelle = this.repTextuelle + " - ";
	this.decompExprDroite(expr);
    }


    /** Décompile la multiplication passée en paramètre en l'entourant de
     * parenthèses.
     * @param expr la multiplication à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteMultiplication(Multiplication expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.decompExprGauche(expr);
	this.repTextuelle = this.repTextuelle + " * ";
	this.decompExprDroite(expr);
    }

    /** Décompile la division passée en paramètre en l'entourant de
     * parenthèses.
     * @param expr la division à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteDivision(Division expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.decompExprGauche(expr);
	this.repTextuelle = this.repTextuelle + " / ";
	this.decompExprDroite(expr);
    }


    /** Décompile le modulo passé en paramètre en l'entourant de
     * parenthèses.
     * @param expr le modulo à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteModulo(Modulo expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.decompExprGauche(expr);
	this.repTextuelle = this.repTextuelle + " % ";
	this.decompExprDroite(expr);
    }


    /** Décompile la conjonction passée en paramètre en l'entourant de
     * parenthèses.
     * @param expr la conjonction à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteConjonction(Conjonction expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.decompExprGauche(expr);
	this.repTextuelle = this.repTextuelle + " and ";
	this.decompExprDroite(expr);
    }


    /** Décompile la disjonction passée en paramètre en l'entourant de
     * parenthèses.
     * @param expr la disjonction à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteDisjonction(Disjonction expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.decompExprGauche(expr);
	this.repTextuelle = this.repTextuelle + " or ";
	this.decompExprDroite(expr);
    }


    /** Décompile le moins unaire passé en paramètre en l'entourant de
     * parenthèses.
     * @param expr le moins unaire à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteMoinsUnaire(MoinsUnaire expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "(-"; 
	// opérande 
	Expression oper = expr.getExpr();
	oper.visite(this);
	this.repTextuelle = this.repTextuelle + ")";
    }

    /** Décompile le not booléen passé en paramètre en l'entourant de
     * parenthèses.
     * @param expr le not à décompiler
     * @throws DecompilateurException si <tt>expr</tt> vaut null.
     */
    public void visiteNegation(Negation expr) 
	throws DecompilateurException, VisiteurException {
	if (expr == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "(not "; 
	// opérande 
	Expression oper = expr.getExpr();
	oper.visite(this);
	this.repTextuelle = this.repTextuelle + ")";
    }



    /** Décompile la comparaison passée en paramètre en l'entourant de parenthèses.
     * @param c la comparaison à décompiler
     * @throws DecompilateurException si <tt>c</tt> vaut null.
     */
    public void visiteEgal(Egal c) 
	throws DecompilateurException, VisiteurException {
	if (c == null)
	    throw new DecompilateurException();
	this.decompExprGauche(c);
	this.repTextuelle = this.repTextuelle + " = ";
	this.decompExprDroite(c);
    }

    /** Décompile la comparaison passée en paramètre en l'entourant de
     * parenthèses.
     * @param c la comparaison à décompiler
     * @throws DecompilateurException si <tt>c</tt> vaut null.
     */
    public void visiteDifferent(Different c) 
	throws DecompilateurException, VisiteurException {
	if (c == null)
	    throw new DecompilateurException();
	this.decompExprGauche(c);
	this.repTextuelle = this.repTextuelle + " /= ";
	this.decompExprDroite(c);
    }


    /** Décompile la comparaison passée en paramètre en l'entourant de
     * parenthèses.
     * @param c la comparaison à décompiler
     * @throws DecompilateurException si <tt>c</tt> vaut null.
     */
    public void visiteSuperieurStrict(SuperieurStrict c) 
	throws DecompilateurException, VisiteurException {
	if (c == null)
	    throw new DecompilateurException();
	this.decompExprGauche(c);
	this.repTextuelle = this.repTextuelle + " > ";
	this.decompExprDroite(c);
    }

    /** Décompile la comparaison passée en paramètre en l'entourant de
     * parenthèses.
     * @param c la comparaison à décompiler
     * @throws DecompilateurException si <tt>c</tt> vaut null.
     */
    public void visiteSuperieurOuEgal(SuperieurOuEgal c) 
	throws DecompilateurException, VisiteurException {
	if (c == null)
	    throw new DecompilateurException();
	this.decompExprGauche(c);
	this.repTextuelle = this.repTextuelle + " >= ";
	this.decompExprDroite(c);
    }

    /** Décompile la comparaison passée en paramètre en l'entourant de
     * parenthèses.
     * @param c la comparaison à décompiler
     * @throws DecompilateurException si <tt>c</tt> vaut null.
     */
    public void visiteInferieurOuEgal(InferieurOuEgal c) 
	throws DecompilateurException, VisiteurException {
	if (c == null)
	    throw new DecompilateurException();
	this.decompExprGauche(c);
	this.repTextuelle = this.repTextuelle + " <= ";
	this.decompExprDroite(c);
    }

    /** Décompile la comparaison passée en paramètre en l'entourant de
     * parenthèses.
     * @param c la comparaison à décompiler
     * @throws DecompilateurException si <tt>c</tt> vaut null.
     */
    public void visiteInferieurStrict(InferieurStrict c) 
	throws DecompilateurException, VisiteurException {
	if (c == null)
	    throw new DecompilateurException();
	this.decompExprGauche(c);
	this.repTextuelle = this.repTextuelle + " < ";
	this.decompExprDroite(c);
    }


    /******************* Instructions *******************/

    /** Décompile la liste d'instructions passée en paramètre.
     * @param linstr la liste d'instructions à décompiler
     */    
    public void visiteListeInstr(ListeInstruction linstr) throws 
						   VisiteurException {
	Iterator it = linstr.iterator();
	while (it.hasNext()) {
	    Instruction i = (Instruction) it.next();
	    i.visite(this);
	}
    }


    /** Décompile l'affectation passé en paramètre.
     * @param a l'affectation à décompiler
     * @throws DecompilateurException si <tt>a</tt> vaut null.
     */    
    public void visiteAffect(Affectation a) throws DecompilateurException, 
						   VisiteurException {
	if (a == null)
	    throw new DecompilateurException();
	// identificateur
	String id = a.getIdent();
	this.repTextuelle = this.repTextuelle + "\n" + id + " := ";
	// expression
	Expression e = a.getExpr();
	e.visite(this);
	this.repTextuelle = this.repTextuelle + ";";
    }

    /** Décompile la lecture passée en paramètre.
     * @param l la lecture à décompiler
     * @throws DecompilateurException si <tt>l</tt> vaut null.
     */    
    public void visiteLecture(Lecture l) throws DecompilateurException, 
						   VisiteurException {	
	if (l == null)
	    throw new DecompilateurException();
	// identificateur
	String id = l.getIdent();
	this.repTextuelle = this.repTextuelle + "\n" + "read " + id + ";";
    }


    /** Décompile la conditionnelle sans else passée en paramètre.
     * @param cond la conditionnelle à décompiler
     * @throws DecompilateurException si <tt>cond</tt> vaut null.
     */    
    public void visiteConditionnelleSimple(ConditionnelleSimple cond) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (cond == null)
	    throw new DecompilateurException();
	// début if
	this.repTextuelle = this.repTextuelle + "\n" + "if (";
	// expression
	Expression expr = cond.getCondition();
	expr.visite(this);
	this.repTextuelle = this.repTextuelle + ") {";
	// partie then
	ListeInstruction li = cond.getListeInstrAlors();
	li.visite(this);
	this.repTextuelle = this.repTextuelle + "\n" + "};";
    }

    /** Décompile la conditionnelle avec else passée en paramètre.
     * @param cond la conditionnelle à décompiler
     * @throws DecompilateurException si <tt>cond</tt> vaut null.
     */    
    public void visiteConditionnelleAvecAlternative(ConditionnelleAvecAlternative cond) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (cond == null)
	    throw new DecompilateurException();
	// début if
	this.repTextuelle = this.repTextuelle + "\n" + "if (";
	// expression
	Expression expr = cond.getCondition();
	expr.visite(this);
	this.repTextuelle = this.repTextuelle + ") {";
	// partie then
	ListeInstruction li = cond.getListeInstrAlors();
	li.visite(this);
	//partie else
	this.repTextuelle = this.repTextuelle + "\n}" + " else {";
	li = cond.getListeInstrSinon();
	li.visite(this);
	this.repTextuelle = this.repTextuelle + "\n" + "};";
    }

    /** Décompile la boucle while passée en paramètre.
     * @param boucle la boucle while à décompiler
     * @throws DecompilateurException si <tt>boucle</tt> vaut null.
     */    
    public void visiteBoucle(Boucle boucle) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (boucle == null)
	    throw new DecompilateurException();
	// début while
	this.repTextuelle = this.repTextuelle + "\n" + "while (";
	// expression
	Expression expr = boucle.getCondition();
	expr.visite(this);
	this.repTextuelle = this.repTextuelle + ") {";
	// partie corps
	ListeInstruction li = boucle.getListeInstr();
	li.visite(this);
	this.repTextuelle = this.repTextuelle + "\n" + "};";
    }

    /** Décompile l'expression d'une impression, avec ajout d'une
     * parenthèse fermante.  
     * @param exp l'expression à décompiler.
     */
    private void decompExprImpr(Expression expr) 
	                          throws VisiteurException {
	expr.visite(this);
	this.repTextuelle = this.repTextuelle + ");";		
    }
    
    /** Décompile l'impression passée en paramètre.
     * @param i l'impression à décompiler
     * @throws DecompilateurException si <tt>i</tt> vaut null.
     */    
    public void visiteImpressionEntierSansSautDeLigne(ImpressionEntierSansSautDeLigne i) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (i == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" + "write(%i,";
	this.decompExprImpr(i.getExpr());
    }

    /** Décompile l'impression passée en paramètre.
     * @param i l'impression à décompiler
     * @throws DecompilateurException si <tt>i</tt> vaut null.
     */    
    public void visiteImpressionEntierAvecSautDeLigne(ImpressionEntierAvecSautDeLigne i) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (i == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" + "writeln(%i,";
	this.decompExprImpr(i.getExpr());
    }


    /** Décompile l'impression passée en paramètre.
     * @param i l'impression à décompiler
     * @throws DecompilateurException si <tt>i</tt> vaut null.
     */    
    public void visiteImpressionBooleenSansSautDeLigne(ImpressionBooleenSansSautDeLigne i) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (i == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" + "write(%b,";
	this.decompExprImpr(i.getExpr());
    }

    /** Décompile l'impression passée en paramètre.
     * @param i l'impression à décompiler
     * @throws DecompilateurException si <tt>i</tt> vaut null.
     */    
    public void visiteImpressionBooleenAvecSautDeLigne(ImpressionBooleenAvecSautDeLigne i) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (i == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" + "writeln(%b,";
	this.decompExprImpr(i.getExpr());
    }

    /** Décompile l'impression passée en paramètre.
     * @param i l'impression à décompiler
     * @throws DecompilateurException si <tt>i</tt> vaut null.
     */    
    public void visiteImpressionChaineSansSautDeLigne(ImpressionChaineSansSautDeLigne i) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (i == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" + "write(%s,";
	// chaine
	String s = i.getChaine();
	this.repTextuelle = this.repTextuelle + "\"" + s + "\"" + ");";
    }

    /** Décompile l'impression passée en paramètre.
     * @param i l'impression à décompiler
     * @throws DecompilateurException si <tt>i</tt> vaut null.
     */    
    public void visiteImpressionChaineAvecSautDeLigne(ImpressionChaineAvecSautDeLigne i) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (i == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" + "writeln(%s,";
	// chaine
	String s = i.getChaine();
	this.repTextuelle = this.repTextuelle + "\"" + s + "\"" + ");";
    }

    /** Décompile l'impression passée en paramètre.
     * @param i l'impression à décompiler
     * @throws DecompilateurException si <tt>i</tt> vaut null.
     */    
    public void visiteImpressionSautDeLigne(ImpressionSautDeLigne i) 
	throws DecompilateurException, 
	       VisiteurException {	
	if (i == null)
	    throw new DecompilateurException();
	this.repTextuelle = this.repTextuelle + "\n" + "writeln;";
    }


}

