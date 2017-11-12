package ava.arbreAbstrait;

/** Addition sur entiers.
 * @author M. Nebut
 * @version 11/07
 */
public class Addition extends Operateur {

    /** Construit une expression étant donnés son opérande
     * gauche et son opérande droit.
     * @param g opérande gauche de l'expression
     * @param d opérande droit de l'expression
     * @throws IllegalArgumentException si <tt>g</tt> et/ou <tt>d</tt>
     * valent null.
     */
    public Addition(Expression g, Expression d) {
	super(g,d);
    }

    /** Permet la visite de cette addition par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cette addition.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteAddition(this);
    }


}