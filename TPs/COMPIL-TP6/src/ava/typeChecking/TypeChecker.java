package ava.typeChecking;

import ava.tableSymboles.*;
import ava.arbreAbstrait.*;

import java.util.Iterator;


/** Contrôle de type pour Ava, qui prend en entrée un arbre abstrait
 * et remplit la table des symboles vide passée en
 * paramètre. L'analyse lève une exception de type
 * <tt>TypeChecking</tt> en cas d'erreur de typage, ne fait rien sinon
 * (à part compléter la table des symboles).
 * @version 04/07 - Revu le 11/10
 * @author M. Nebut - Modifie par F.Bellano
 */
public class TypeChecker implements Visiteur { // COMPLETER

	// the symbol table to be filled
	private TableSymboles tableSymboles;

	// the abstract tree
	private Programme program;

	// the last expression's type visited
	private Type typeLastExpression;


	/** Builds a type analyzer which fills the symbol table tableSymboles.
	 * @param tableSymboles the table to be filled     
	 * @param p the abstract tree to be analyzed
	 */
	public TypeChecker(TableSymboles tableSymboles, Programme p) {
		this.tableSymboles = tableSymboles;
		this.program = p;
		this.typeLastExpression = Type.NOTYPE;
	}

	/** Executes the type analysis.
	 * @throws TypeCheckingException if p is badly typed.
	 */
	public void typeCheck() throws TypeCheckingException, VisiteurException {
		this.visiteProgramme(this.program);
	}

	/** Visite d'un programme. */
	public void visiteProgramme(Programme p) throws TypeCheckingException, VisiteurException {
		String nom = p.getNom();
		if ("".equals(nom)) {
			throw new TypeCheckingException("le programme ne peut pas être nul");
		}
		AttributsIdentificateur att = this.tableSymboles.ajoutIdentificateur(nom);
		att.setType(Type.PROGRAM);
		ListeDeclaration ldecl = p.getListeDecl();
		this.visiteListeDecl(ldecl);

		ListeInstruction linst = p.getListeInstr();
		this.visiteListeInstr(linst);
		this.typeLastExpression = Type.PROGRAM;
	}


	/************************ Déclarations ************************/

	/** Visite d'une liste de déclarations. */
	public void visiteListeDecl(ListeDeclaration ldecl) throws VisiteurException {
		if (ldecl == null) {
			throw new VisiteurException("La liste est null");
		}
		Iterator it = ldecl.iterator();
		while (it.hasNext()) {
			Declaration uneDeclaration = (Declaration) it.next();
			uneDeclaration.visite(this);
		}
	}

	/** Visite d'une déclaration de type entier. */
	public void visiteDeclInt(DeclarationEntier decl) throws VisiteurException {
		String name = decl.getIdent();
		if (this.tableSymboles.contientIdentificateur(name))
			throw new TypeCheckingException("la variable " + name
					+ " a deja ete declaree");
		AttributsIdentificateur att =
			this.tableSymboles.ajoutIdentificateur(name);
		att.setType(Type.ENTIER);
	}

	/** Visite d'une déclaration de type booléen. */
	public void visiteDeclBool(DeclarationBooleen decl) throws VisiteurException {
		String name = decl.getIdent();
		if (this.tableSymboles.contientIdentificateur(name)) {
			throw new TypeCheckingException("la variable " + name
					+ " a deja ete declaree");
		}
		AttributsIdentificateur att = this.tableSymboles.ajoutIdentificateur(name);
		att.setType(Type.BOOLEEN);
	}

	/************************ Instructions ************************/

	/** Visite d'une liste d'instructions. */
	public void visiteListeInstr(ListeInstruction linstr) throws VisiteurException {
		if (linstr == null) {
			throw new TypeCheckingException("Parametre NULL de la liste d instructions.");
		}
		Iterator it = linstr.iterator();
		while (it.hasNext()) {
			Instruction i = (Instruction) it.next();
			i.visite(this);
		}
	}

	/** Visite d'une affectation. */
	public void visiteAffect(Affectation a) throws VisiteurException {
		Expression e = a.getExpr();
		String var = a.getIdent();
		if (a == null) {
			throw new TypeCheckingException("Parametre NULL de l'affectation");
		}
		if (!this.tableSymboles.contientIdentificateur(var)) {
			throw new TypeCheckingException("La variable "+var+" n'a pas ete declaree");
		}
		AttributsIdentificateur attribut = this.tableSymboles.getAttributsIdentificateur(var);
		Type typeAttribut = attribut.getType();
		e.visite(this);
		if (typeAttribut != this.typeLastExpression) {
			throw new TypeCheckingException(var + " est de type " + typeAttribut + " alors que l'affectation est de type " + this.typeLastExpression);
		}
	}


	/** Visite d'une lecture. */
	public void visiteLecture(Lecture l) throws VisiteurException {
		if (l == null) {
			throw new TypeCheckingException("parametre de la lecture null");
		}
		String var = l.getIdent();
		if (!this.tableSymboles.contientIdentificateur(var)) {
			throw new TypeCheckingException("La variable " + var + " n'a pas ete declaree");
		}
		AttributsIdentificateur attribut = this.tableSymboles.getAttributsIdentificateur(var);
		if (attribut.getType() != Type.ENTIER) {
			throw new TypeCheckingException("On ne peut lire que des type entier, et "+var+" est de type "+attribut.getType().toString());
		}
	}

	/** Visite d'une conditionnelle sans partie else. */
	public void visiteConditionnelleSimple(ConditionnelleSimple l) 
	throws VisiteurException {
		if (l == null) {
			throw new TypeCheckingException("Parametre de la condition null");
		}
		Expression cond = l.getCondition();
		cond.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN) {
			throw new TypeCheckingException("La condition doit etre du type BOOLEEN");
		}
		ListeInstruction linst = l.getListeInstrAlors();
		linst.visite(this);
	}

	/** Visite d'une conditionnelle avec partie else. */
	public void visiteConditionnelleAvecAlternative(ConditionnelleAvecAlternative l) 
	throws VisiteurException {
		if (l == null) {
			throw new TypeCheckingException("Parametre de la condition null");
		}
		Expression cond = l.getCondition();
		cond.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN) {
			throw new TypeCheckingException("La condition doit etre du type BOOLEEN");
		}
		ListeInstruction linstAlors = l.getListeInstrAlors();
		linstAlors.visite(this);
		ListeInstruction linstSinon = l.getListeInstrSinon();
		linstSinon.visite(this);
	}

	/** Visite d'une boucle while. */
	public void visiteBoucle(Boucle b) 
	throws VisiteurException {
		if (b == null) {
			throw new TypeCheckingException("Parametre de la boucle null");
		}
		Expression cond = b.getCondition();
		cond.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN) {
			throw new TypeCheckingException("La condition doit etre du type BOOLEEN");
		}
		ListeInstruction linst = b.getListeInstr();
		linst.visite(this);
	}

	/** Visite d'une impression de type entier. */
	public void visiteImpressionEntierSansSautDeLigne(ImpressionEntierSansSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression null");
		}
		Expression expr = i.getExpr();
		expr.visite(this);
		if (this.typeLastExpression != Type.ENTIER) {
			throw new TypeCheckingException("L'expression doit etre du type ENTIER");
		}
	}

	/** Visite d'une impression avec retour à la ligne de type entier. */
	public void visiteImpressionEntierAvecSautDeLigne(ImpressionEntierAvecSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression null");
		}
		ImpressionEntierSansSautDeLigne imp = new ImpressionEntierSansSautDeLigne(i.getExpr());
		imp.visite(this);
	}

	/** Visite d'une impression de type booleen. */
	public void visiteImpressionBooleenSansSautDeLigne(ImpressionBooleenSansSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression null");
		}
		Expression expr = i.getExpr();
		expr.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN) {
			throw new TypeCheckingException("L'expression doit etre du type BOOLEEN");
		}
	}

	/** Visite d'une impression avec retour à la ligne de type booleen. */
	public void visiteImpressionBooleenAvecSautDeLigne(ImpressionBooleenAvecSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression null");
		}
		ImpressionBooleenSansSautDeLigne newImpression = new ImpressionBooleenSansSautDeLigne(i.getExpr());
		newImpression.visite(this);
	}

	/** Visite d'une impression de type chaine. */
	public void visiteImpressionChaineSansSautDeLigne(ImpressionChaineSansSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression null");
		}
// On n'a plus rien a faire car c'est l'impression d'une chaine de caractere (qui sera controle par l'analyseur lexical
	}

	/** Visite d'une impression avec retour à la ligne de type chaine. */
	public void visiteImpressionChaineAvecSautDeLigne(ImpressionChaineAvecSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression null");
		}
// On n'a plus rien a faire car c'est l'impression d'une chaine de caractere (qui sera controle par l'analyseur lexical
	}


	/** Visite d'une impression de retour à la ligne. */
	public void visiteImpressionSautDeLigne(ImpressionSautDeLigne i)
	throws VisiteurException {
		// On ne doit rien faire, pas de controle de type car c'est l'affichage d'un saut de ligne
	}


	/************************ Expressions ************************/

	/** Visite d'un entier. */
	public void visiteEntier(Entier e) throws VisiteurException {
		this.typeLastExpression = Type.ENTIER;
	}

	/** Visite d'un identificateur. */
	public void visiteIdentExpr(IdentExpr i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur null");
		String nom = i.getNom();
		if (!this.tableSymboles.contientIdentificateur(nom))
			throw new TypeCheckingException("La variable "+nom+" n'a pas ete declaree");
		AttributsIdentificateur attribut = this.tableSymboles.getAttributsIdentificateur(nom);
		this.typeLastExpression = attribut.getType();
	}

	/** Visite d'une constante vrai. */
	public void visiteExprTrue(ExprTrue i) throws VisiteurException {
		this.typeLastExpression = Type.BOOLEEN;
	}

	/** Visite d'une constante faux. */
	public void visiteExprFalse(ExprFalse i) throws VisiteurException {
		this.typeLastExpression = Type.BOOLEEN;
	}

	/** Visite d'une addition. */
	public void visiteAddition(Addition i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur null");
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
	}

	/** Visite d'une soustraction. */
	public void visiteSoustraction(Soustraction i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur null");
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
	}

	/** Visite d'une multiplication. */
	public void visiteMultiplication(Multiplication i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur null");
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
	}

	/** Visite d'une division. */
	public void visiteDivision(Division i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur null");
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
	}


	/** Visite d'un modulo. */
	public void visiteModulo(Modulo i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur null");
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite est " + this.typeLastExpression.toString() +", il doit etre de type ENTIER");
	}


	/** Visite d'un moins unaire. */
	public void visiteMoinsUnaire(MoinsUnaire i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre du moins unaire null");
		Expression expr = i.getExpr();
		expr.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'expression est "+this.typeLastExpression.toString()+", il doit etre ENTIER");
	}

	/** Visite d'une négation. */
	public void visiteNegation(Negation i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de la negation null");
		Expression expr = i.getExpr();
		expr.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN)
			throw new TypeCheckingException("Le type doit etre de type BOOLEEN");
	}

	/** Visite d'une disjonction. */
	public void visiteDisjonction(Disjonction i) throws VisiteurException {
		if (i==null) 
			throw new TypeCheckingException("Parametre de la disjonction null");
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN)
			throw new TypeCheckingException("L'operande gauche de la disjonction doit etre de type BOOLEEN");
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN)
			throw new TypeCheckingException("L'operande droite de la disjonction doit etre de type BOOLEEN");
	}

	/** Visite d'une conjonction. */
	public void visiteConjonction(Conjonction i) throws VisiteurException {
		if (i==null) 
			throw new TypeCheckingException("Parametre de la conjonction null");
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN)
			throw new TypeCheckingException("L'operande gauche de la conjonction doit etre de type BOOLEEN");
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.BOOLEEN)
			throw new TypeCheckingException("L'operande droite de la conjonction doit etre de type BOOLEEN");
	}

	/** Visite d'une égalité. */
	public void visiteEgal(Egal e) throws VisiteurException {
		if (e==null)
			throw new TypeCheckingException("Parametre de l'égalité null");
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche de l'egalite n'est pas de type ENTIER");
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite de l'egalite n'est pas de type ENTIER");
		this.typeLastExpression = Type.BOOLEEN;
	}

	/** Visite d'une différence. */
	public void visiteDifferent(Different e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre de la difference null");
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche de la difference n'est pas de type ENTIER");
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite de la difference n'est pas de type ENTIER");
		this.typeLastExpression = Type.BOOLEEN;
	}

	/** Visite d'un supérieur strict. */
	public void visiteSuperieurStrict(SuperieurStrict e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre du strictement superieur null");
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche du strictement superieur n'est pas de type ENTIER");
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite du strictement superieur n'est pas de type ENTIER");
		this.typeLastExpression = Type.BOOLEEN;
	}

	/** Visite d'un supérieur ou egal. */
	public void visiteSuperieurOuEgal(SuperieurOuEgal e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre du superieur ou egal null");
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche du superieur ou egal n'est pas de type ENTIER");
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite du superieur ou egal n'est pas de type ENTIER");
		this.typeLastExpression = Type.BOOLEEN;
	}

	/** Visite d'un inferieur ou egal. */
	public void visiteInferieurOuEgal(InferieurOuEgal e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre du inferieur ou egal null");
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche du inferieur ou egal n'est pas de type ENTIER");
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite du inferieur ou egal n'est pas de type ENTIER");
		this.typeLastExpression = Type.BOOLEEN;
	}

	/** Visite d'un inferieur strict. */
	public void visiteInferieurStrict(InferieurStrict e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre du inferieur strictement null");
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche du inferieur strictement n'est pas de type ENTIER");
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		if (this.typeLastExpression != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite du inferieur strictement n'est pas de type ENTIER");
		this.typeLastExpression = Type.BOOLEEN;
	}
}
