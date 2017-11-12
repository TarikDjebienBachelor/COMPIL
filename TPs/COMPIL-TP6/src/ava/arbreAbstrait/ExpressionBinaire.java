package ava.arbreAbstrait;


/** Expression d'un programme Ava basée sur un opérateur binaire.
 * @author M. Nebut
 * @version 11/07
 */
public abstract class ExpressionBinaire implements Expression {

    // opérande gauche
    private Expression gauche;

    // opérande droite
    private Expression droit;


    /** Construit une expression binaire étant donnés son opérande
     * gauche et son opérande droit.
     * @param g opérande gauche de l'expression
     * @param d opérande droit de l'expression
     * @throws IllegalArgumentException si <tt>g</tt> et/ou <tt>d</tt>
     * valent null.
     */
    public ExpressionBinaire(Expression g, Expression d) {
	if (g == null || d == null)
	    throw new IllegalArgumentException();
	this.gauche = g;
	this.droit = d;
    }

    /** Retourne l'expression opérande gauche de cette expression binaire.
     * @return l'opérande gauche de cette expression
     */
    public Expression getExprGauche() {
	return this.gauche;
    }

    /** Retourne l'expression opérande droit de cette expression binaire.
     * @return l'opérande droit de cette expression
     */
    public Expression getExprDroite() {
	return this.droit;
    }

    /** Permet la visite de cette expression binaire par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cet entier.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public abstract void visite(Visiteur v) throws VisiteurException;
 

}