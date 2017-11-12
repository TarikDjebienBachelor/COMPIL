package ava.arbreAbstrait;


/** Un visiteur pour parcourir un arbre abstrait représentant un
 * programme Ava. À chaque feuille de l'arbre « visitable » correspond
 * une méthode <tt>visitX</tt> qui permet de visiter cette
 * feuille. Dans le code de la classe représentant la feuille on
 * trouve une méthode <tt>visite(Visiteur v)</tt> qui permet d'appeler
 * <tt>visitX</tt>. Les méthodes <tt>visitX</tt> ne retournent pas de
 * résultat : on utilisera par des attributs pour passer des valeurs.
 *
 * @author M. Nebut
 * @version 11/07
 */
public interface Visiteur {

    /** Visite d'un programme. */
    public void visiteProgramme(Programme p) throws VisiteurException;


    /************************ Déclarations ************************/

    /** Visite d'une liste de déclarations. */
    public void visiteListeDecl(ListeDeclaration ldecl) throws VisiteurException;

    /** Visite d'une déclaration de type entier. */
    public void visiteDeclInt(DeclarationEntier decl) throws VisiteurException;

    /** Visite d'une déclaration de type booléen. */
    public void visiteDeclBool(DeclarationBooleen decl) throws VisiteurException;

    /************************ Instructions ************************/

    /** Visite d'une liste d'instructions. */
    public void visiteListeInstr(ListeInstruction linstr) throws VisiteurException;

    /** Visite d'une affectation. */
    public void visiteAffect(Affectation a) throws VisiteurException;


    /** Visite d'une lecture. */
    public void visiteLecture(Lecture l) throws VisiteurException;

    /** Visite d'une conditionnelle sans partie else. */
    public void visiteConditionnelleSimple(ConditionnelleSimple l) 
	throws VisiteurException;

    /** Visite d'une conditionnelle avec partie else. */
    public void visiteConditionnelleAvecAlternative(ConditionnelleAvecAlternative l) 
	throws VisiteurException;

    /** Visite d'une boucle while. */
    public void visiteBoucle(Boucle b) 
	throws VisiteurException;

    /** Visite d'une impression de type entier. */
    public void visiteImpressionEntierSansSautDeLigne(ImpressionEntierSansSautDeLigne i)
	throws VisiteurException;

    /** Visite d'une impression avec retour à la ligne de type entier. */
    public void visiteImpressionEntierAvecSautDeLigne(ImpressionEntierAvecSautDeLigne i)
	throws VisiteurException;

    /** Visite d'une impression de type booleen. */
    public void visiteImpressionBooleenSansSautDeLigne(ImpressionBooleenSansSautDeLigne i)
	throws VisiteurException;

    /** Visite d'une impression avec retour à la ligne de type booleen. */
    public void visiteImpressionBooleenAvecSautDeLigne(ImpressionBooleenAvecSautDeLigne i)
	throws VisiteurException;

    /** Visite d'une impression de type chaine. */
    public void visiteImpressionChaineSansSautDeLigne(ImpressionChaineSansSautDeLigne i)
	throws VisiteurException;

    /** Visite d'une impression avec retour à la ligne de type chaine. */
    public void visiteImpressionChaineAvecSautDeLigne(ImpressionChaineAvecSautDeLigne i)
	throws VisiteurException;


    /** Visite d'une impression de retour à la ligne. */
    public void visiteImpressionSautDeLigne(ImpressionSautDeLigne i)
	throws VisiteurException;


    /************************ Expressions ************************/

    /** Visite d'un entier. */
    public void visiteEntier(Entier e) throws VisiteurException;

    /** Visite d'un identificateur. */
    public void visiteIdentExpr(IdentExpr i) throws VisiteurException;

    /** Visite d'une constante vrai. */
    public void visiteExprTrue(ExprTrue i) throws VisiteurException;

    /** Visite d'une constante faux. */
    public void visiteExprFalse(ExprFalse i) throws VisiteurException;

    /** Visite d'une addition. */
    public void visiteAddition(Addition i) throws VisiteurException;

    /** Visite d'une soustraction. */
    public void visiteSoustraction(Soustraction i) throws VisiteurException;

    /** Visite d'une multiplication. */
    public void visiteMultiplication(Multiplication i) throws VisiteurException;

    /** Visite d'une division. */
    public void visiteDivision(Division i) throws VisiteurException;


    /** Visite d'un modulo. */
    public void visiteModulo(Modulo i) throws VisiteurException;


    /** Visite d'un moins unaire. */
    public void visiteMoinsUnaire(MoinsUnaire i) throws VisiteurException;

    /** Visite d'une négation. */
    public void visiteNegation(Negation i) throws VisiteurException;

    /** Visite d'une disjonction. */
    public void visiteDisjonction(Disjonction i) throws VisiteurException;

    /** Visite d'une conjonction. */
    public void visiteConjonction(Conjonction i) throws VisiteurException;

    /** Visite d'une égalité. */
    public void visiteEgal(Egal e) throws VisiteurException;

    /** Visite d'une différence. */
    public void visiteDifferent(Different e) throws VisiteurException;

    /** Visite d'un supérieur strict. */
    public void visiteSuperieurStrict(SuperieurStrict e) throws VisiteurException;

    /** Visite d'un supérieur ou egal. */
    public void visiteSuperieurOuEgal(SuperieurOuEgal e) throws VisiteurException;

    /** Visite d'un inferieur ou egal. */
    public void visiteInferieurOuEgal(InferieurOuEgal e) throws VisiteurException;
    
    /** Visite d'un inferieur strict. */
    public void visiteInferieurStrict(InferieurStrict e) throws VisiteurException;


}
