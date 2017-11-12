package ava.arbreAbstrait;


/** Expression d'un programme Ava basée sur un opérateur unaire.
 * @author M. Nebut
 * @version 11/07
 */
public abstract class ExpressionUnaire implements Expression {

    // opérande 
    private Expression expr;

    /** Construit une expression unaire étant donné son opérande.
     * @param e opérande l'expression
     * @throws IllegalArgumentException si <tt>e</tt> vaut null.
     */
    public ExpressionUnaire(Expression e) {
	if (e == null)
	    throw new IllegalArgumentException();
	this.expr = e;
    }

    /** Retourne l'expression opérande cette expression unaire.
     * @return l'opérande de cette expression
     */
    public Expression getExpr() {
	return this.expr;
    }


    /** Permet la visite de cette expression unaire par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cet entier.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public abstract void visite(Visiteur v) throws VisiteurException;
 

}