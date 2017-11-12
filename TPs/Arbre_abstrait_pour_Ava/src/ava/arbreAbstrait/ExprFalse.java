package ava.arbreAbstrait;




/** Constante false dans un programme
 * Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class ExprFalse implements Expression {


    /** Permet à un visiteur de visiter cette constante.
     * @param v un visiteur pour cette constante
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteExprFalse(this);
    }



}
