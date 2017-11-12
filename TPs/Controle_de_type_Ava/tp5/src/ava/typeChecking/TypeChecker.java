package ava.typeChecking;
import ava.tableSymboles.*;
import ava.arbreAbstrait.*;
import java.util.Iterator;


/** Contrôle de type pour Ava, qui prend en entrée un arbre abstrait
 * et remplit la table des symboles vide passée en
 * paramètre. L'analyse lève une exception de type
 * <tt>TypeChecking</tt> en cas d'erreur de typage, ne fait rien sinon
 * (à part compléter la table des symboles).
 * @author D.Tarik-L3-S5-Groupe2
 */
// Pour la question du TP : Design pattern visitor => typeChecker est un visiteur
public class TypeChecker implements Visiteur { // en java TypeChecker implementera donc le type Visiteur

    // le dernier type d'expression qui a été visité pour garder la trace du précédent
    private Type typeExpressionPrecedente;

    // the symbol table to be filled
    private TableSymboles tableSymboles; // table de symbole à remplir au fur et a mesure des visites

    // the abstract tree
    private Programme program; // le programme lu

	/** Visite d'un programme. */
	public void visiteProgramme(Programme p) throws TypeCheckingException, VisiteurException {
		String progName = p.getNom();
		String chaineVide = "";
		if (chaineVide.equals(progName)) {
			throw new TypeCheckingException("error: le nom du programme ne peut etre vide !! ");
		}
		AttributsIdentificateur ai = this.tableSymboles.ajoutIdentificateur(progName);
		ai.setType(Type.PROGRAM);
		ListeDeclaration ld = p.getListeDecl();
		this.visiteListeDecl(ld);
		ListeInstruction li = p.getListeInstr();
		this.visiteListeInstr(li);
		this.typeExpressionPrecedente = Type.PROGRAM;
	}


    /************************ Déclarations ************************/

    	/** Visite d'une liste de déclarations. */
	public void visiteListeDecl(ListeDeclaration ldecl) throws VisiteurException {
		if (ldecl == null) {
			throw new VisiteurException("La liste de déclaration est vide");
		}
		Iterator it = ldecl.iterator();
		while (it.hasNext()) {
			Declaration d = (Declaration) it.next();
			d.visite(this);
		}
	}

	/** Visite d'une déclaration de type entier. */
	public void visiteDeclInt(DeclarationEntier decl) throws VisiteurException {
		String varName = decl.getIdent();
		if (this.tableSymboles.contientIdentificateur(varName))
			throw new TypeCheckingException("error: la variable " + varName + " a deja ete declaree prcedemment !! ");
		AttributsIdentificateur a = this.tableSymboles.ajoutIdentificateur(varName);
		a.setType(Type.ENTIER);
	}



	/** Visite d'une déclaration de type booléen. */
	public void visiteDeclBool(DeclarationBooleen decl) throws VisiteurException {
		String varName = decl.getIdent();
		if (this.tableSymboles.contientIdentificateur(varName)) {
			throw new TypeCheckingException("error: la variable " + varName
					+ " a deja ete declaree precedemment");
		}
		AttributsIdentificateur a = this.tableSymboles.ajoutIdentificateur(varName);
		a.setType(Type.BOOLEEN);
	}

    /************************ Instructions ************************/

	/** Visite d'une liste d'instructions. */
	public void visiteListeInstr(ListeInstruction linstr) throws VisiteurException {
		if (linstr == null) {
			throw new TypeCheckingException("la liste d instructions est vide.");
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
		String exprName = a.getIdent();
		if (a == null) {
			throw new TypeCheckingException("affectation de parametre vide !!");
		}
		if (!this.tableSymboles.contientIdentificateur(exprName)) {
			throw new TypeCheckingException("error: La variable "+exprName+" n'a pas ete declaree precedemment.");
		}
		AttributsIdentificateur aI = this.tableSymboles.getAttributsIdentificateur(exprName);
		Type typeAttribut = aI.getType();
		e.visite(this);
		if (typeAttribut != this.typeExpressionPrecedente) {
			throw new TypeCheckingException("error : "+exprName + " est de type " + typeAttribut + " mais votre affectation est de type " + this.typeExpressionPrecedente);
		}
	}

	/** Visite d'une lecture. */
	public void visiteLecture(Lecture l) throws VisiteurException {
		if (l == null) {
			throw new TypeCheckingException("parametre de la lecture vide");
		}
		String readName = l.getIdent();
		if (!this.tableSymboles.contientIdentificateur(readName)) {
			throw new TypeCheckingException("La variable " + readName + " n'a pas ete declaree precedemment");
		}
		AttributsIdentificateur a = this.tableSymboles.getAttributsIdentificateur(readName);
		if (a.getType() != Type.ENTIER) {
			throw new TypeCheckingException("error : "+readName+" est de type "+a.getType().toString()+", seul les entiers peuvent etre lus!");
		}
	}

	/** Visite d'une conditionnelle sans partie else. */
	public void visiteConditionnelleSimple(ConditionnelleSimple l)
	throws VisiteurException {
		if (l == null) {
			throw new TypeCheckingException("Parametre de la condition vide");
		}
		Expression cond = l.getCondition();
		cond.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN) {
			throw new TypeCheckingException("error : la conditionnelle If de AVA doit etre du type booleenne");
		}
		ListeInstruction li = l.getListeInstrAlors();
		li.visite(this);
	}

	/** Visite d'une conditionnelle avec partie else. */
	public void visiteConditionnelleAvecAlternative(ConditionnelleAvecAlternative l)
	throws VisiteurException {
		if (l == null) {
			throw new TypeCheckingException("Parametre de la condition vide");
		}
		Expression cond = l.getCondition();
		cond.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN) {
			throw new TypeCheckingException("error : la conditionnelle If Else de AVA doit etre du type booleenne!!");
		}
		ListeInstruction liAlors = l.getListeInstrAlors();
		liAlors.visite(this);
		ListeInstruction liSinon = l.getListeInstrSinon();
		liSinon.visite(this);
	}

	/** Visite d'une boucle while. */
	public void visiteBoucle(Boucle b)
	throws VisiteurException {
		if (b == null) {
			throw new TypeCheckingException("Parametre de la boucle vide");
		}
		Expression laCondition = b.getCondition();
		laCondition.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN) {
			throw new TypeCheckingException("La condition de l'iteration Loop de AVA doit etre du type booleenne");
		}
		ListeInstruction li = b.getListeInstr();
		li.visite(this);
	}

	/** Visite d'une impression de type entier. */
	public void visiteImpressionEntierSansSautDeLigne(ImpressionEntierSansSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression d'entier vide");
		}
		Expression e = i.getExpr();
		e.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER) {
			throw new TypeCheckingException("error : L'expression de l'impression d'un entier doit etre du type ENTIER !");
		}
	}

	/** Visite d'une impression avec retour à la ligne de type entier. */
	public void visiteImpressionEntierAvecSautDeLigne(ImpressionEntierAvecSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression d'entier vide");
		}
		ImpressionEntierSansSautDeLigne iESSDL = new ImpressionEntierSansSautDeLigne(i.getExpr());
		iESSDL.visite(this);
	}

	/** Visite d'une impression de type booleen. */
	public void visiteImpressionBooleenSansSautDeLigne(ImpressionBooleenSansSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression de booleen vide");
		}
		Expression e = i.getExpr();
		e.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN) {
			throw new TypeCheckingException("error : L'expression de l'impression d'un booleen doit etre du type BOOLEEN !");
		}
	}

	/** Visite d'une impression avec retour à la ligne de type booleen. */
	public void visiteImpressionBooleenAvecSautDeLigne(ImpressionBooleenAvecSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression de booleen vide");
		}
		ImpressionBooleenSansSautDeLigne newImpression = new ImpressionBooleenSansSautDeLigne(i.getExpr());
		newImpression.visite(this);
	}

	/** Visite d'une impression de type chaine. */
	public void visiteImpressionChaineSansSautDeLigne(ImpressionChaineSansSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression d'une chaine de caractere est vide");
		}
     /* Concernant l'impression d'une chaine de caractere, c'est l'analyseur lexical et syntaxique qui se chargent de gerer la sémantique de la chaine*/
	}

 	/** Visite d'une impression avec retour à la ligne de type chaine. */
	public void visiteImpressionChaineAvecSautDeLigne(ImpressionChaineAvecSautDeLigne i)
	throws VisiteurException {
		if (i == null) {
			throw new TypeCheckingException("Parametre de l'impression de votre chaine de caractere vide");
		}
     /* idem que ci dessus */
	}


	/** Visite d'une impression de retour à la ligne. */
	public void visiteImpressionSautDeLigne(ImpressionSautDeLigne i)
	throws VisiteurException {
		// rien à faire, ici aucun controle de type, en effet, c'est juste un saut de ligne sur la sortie standard.
	}


    /************************ Expressions ************************/

	/** Visite d'un entier. */
	public void visiteEntier(Entier e) throws VisiteurException {
		this.typeExpressionPrecedente = Type.ENTIER;
	}

	/** Visite d'un identificateur. */
	public void visiteIdentExpr(IdentExpr i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur est vide");
		String identName = i.getNom();
		if (!this.tableSymboles.contientIdentificateur(identName))
			throw new TypeCheckingException("error : La variable "+identName+" n'a pas ete declaree precedemment");
		AttributsIdentificateur a = this.tableSymboles.getAttributsIdentificateur(identName);
		this.typeExpressionPrecedente = a.getType();
	}

	/** Visite d'une constante vrai. */
	public void visiteExprTrue(ExprTrue i) throws VisiteurException {
		this.typeExpressionPrecedente = Type.BOOLEEN;
	}

	/** Visite d'une constante faux. */
	public void visiteExprFalse(ExprFalse i) throws VisiteurException {
		this.typeExpressionPrecedente = Type.BOOLEEN;
	}

	/** Visite d'une addition. */
	public void visiteAddition(Addition i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur d'addition vaut null");
		Expression eGauche = i.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
		Expression eDroite = i.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
	}

	/** Visite d'une soustraction. */
	public void visiteSoustraction(Soustraction i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur de soustraction vaut null");
		Expression eGauche = i.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
		Expression eDroite = i.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
	}

	/** Visite d'une multiplication. */
	public void visiteMultiplication(Multiplication i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur de multiplication vaut null");
		Expression eGauche = i.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
		Expression eDroite = i.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
	}

	/** Visite d'une division. */
	public void visiteDivision(Division i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur de division vaut null");
		Expression eGauche = i.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
		Expression eDroite = i.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
	}


	/** Visite d'un modulo. */
	public void visiteModulo(Modulo i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("Parametre de l'identificateur du modulo vaut null");
		Expression eGauche = i.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande gauche :" + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
		Expression eDroite = i.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'operande droite : " + this.typeExpressionPrecedente.toString() +", doit etre de type ENTIER");
	}


	/** Visite d'un moins unaire. */
	public void visiteMoinsUnaire(MoinsUnaire i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("le parametre du moins unaire vaut null");
		Expression e = i.getExpr();
		e.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			throw new TypeCheckingException("Le type de l'expression : "+this.typeExpressionPrecedente.toString()+", doit etre ENTIER");
	}

	/** Visite d'une négation. */
	public void visiteNegation(Negation i) throws VisiteurException {
		if (i == null)
			throw new TypeCheckingException("le parametre de la negation vaut null");
		Expression e = i.getExpr();
		e.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN)
			throw new TypeCheckingException("Le type de la négation doit etre booleen !");
	}

	/** Visite d'une disjonction. */
	public void visiteDisjonction(Disjonction i) throws VisiteurException {
		if (i==null)
			throw new TypeCheckingException("Parametre de la disjonction null");
		Expression eGauche = i.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN)
			throw new TypeCheckingException("L'operande gauche de la disjonction doit etre booleen");
		Expression eDroite = i.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN)
			throw new TypeCheckingException("L'operande droite de la disjonction doit etre booleen");
	}

	/** Visite d'une conjonction. */
	public void visiteConjonction(Conjonction i) throws VisiteurException {
		if (i==null)
			throw new TypeCheckingException("Parametre de la conjonction null");
		Expression eGauche = i.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN)
			throw new TypeCheckingException("L'operande gauche de la conjonction doit etre booleen");
		Expression eDroite = i.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.BOOLEEN)
			throw new TypeCheckingException("L'operande droite de la conjonction doit etre booleen");
	}

	/** Visite d'une égalité. */
	public void visiteEgal(Egal e) throws VisiteurException {
		if (e==null)
			throw new TypeCheckingException("Parametre de l'égalité null");
		Expression eGauche = e.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("error : L expression gauche de l'egalite != entier");
		Expression eDroite = e.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("error : L expression droite de l'egalite != entier");
		this.typeExpressionPrecedente = Type.BOOLEEN;
	}

	/** Visite d'une différence. */
	public void visiteDifferent(Different e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre de la difference null");
		Expression eGauche = e.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche de la difference != ENTIER");
		Expression eDroite = e.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite de la difference != ENTIER");
		this.typeExpressionPrecedente = Type.BOOLEEN;
	}

	/** Visite d'un supérieur strict. */
	public void visiteSuperieurStrict(SuperieurStrict e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre du strictement superieur null");
		Expression eGauche = e.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche du strictement superieur !=  ENTIER");
		Expression eDroite = e.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite du strictement superieur != ENTIER");
		this.typeExpressionPrecedente = Type.BOOLEEN;
	}

	/** Visite d'un supérieur ou egal. */
	public void visiteSuperieurOuEgal(SuperieurOuEgal e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre du superieur ou egal null");
		Expression eGauche = e.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche du superieur ou egal != ENTIER");
		Expression eDroite = e.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite du superieur ou egal != ENTIER");
		this.typeExpressionPrecedente = Type.BOOLEEN;
	}

	/** Visite d'un inferieur ou egal. */
	public void visiteInferieurOuEgal(InferieurOuEgal e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre du inferieur ou egal null");
		Expression eGauche = e.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche du inferieur ou egal != ENTIER");
		Expression eDroite = e.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite du inferieur ou egal != ENTIER");
		this.typeExpressionPrecedente = Type.BOOLEEN;
	}

	/** Visite d'un inferieur strict. */
	public void visiteInferieurStrict(InferieurStrict e) throws VisiteurException {
		if (e == null)
			throw new TypeCheckingException("Parametre du inferieur strictement null");
		Expression eGauche = e.getExprGauche();
		eGauche.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression gauche du inferieur strictement != ENTIER");
		Expression eDroite = e.getExprDroite();
		eDroite.visite(this);
		if (this.typeExpressionPrecedente != Type.ENTIER)
			 throw new TypeCheckingException("L expression droite du inferieur strictement != ENTIER");
		this.typeExpressionPrecedente = Type.BOOLEEN;
	}

    /** Builds a type analyzer which fills the symbol table tableSymboles.
     * @param tableSymboles the table to be filled
     * @param p the abstract tree to be analyzed
     */
    public TypeChecker(TableSymboles tableSymboles, Programme p) {
	this.tableSymboles = tableSymboles;
	this.program = p;
	this.typeExpressionPrecedente = Type.NOTYPE;
    }

    /** Executes the type analysis.
     * @throws TypeCheckingException if p is badly typed.
     */
    public void typeCheck() throws TypeCheckingException, VisiteurException {
       this.visiteProgramme(this.program);
    }

}
