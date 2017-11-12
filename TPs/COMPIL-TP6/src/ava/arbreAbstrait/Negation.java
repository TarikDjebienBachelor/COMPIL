package ava.arbreAbstrait;

/** Négation booléenne.
 * @author M. Nebut
 * @version 11/07
 */
public class Negation extends ExpressionUnaire {

    /** Construit une prise d'opposé étant donné son opérande
     * @param e opérande dont on veut l'opposé 
     * @throws IllegalArgumentException si <tt>e</tt> vaut null.
     */
    public Negation(Expression e) {
	super(e);
    }

    /** Permet la visite de cette prise d'opposé par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cette prose d'opposé.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteNegation(this);
    }

}