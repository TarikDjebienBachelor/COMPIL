package ava.arbreAbstrait;



/** Opérateur inf strict dans un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class InferieurStrict extends Comparaison {

    /** Construit une comparaison étant donnés son opérande
     * gauche et son opérande droit.
     * @param g opérande gauche de la comparaison
     * @param d opérande droit de la comparaison
     * @throws IllegalArgumentException si <tt>g</tt> et/ou <tt>d</tt>
     * valent null.
     */
    public InferieurStrict(Expression g, Expression d) {
	super(g,d);
    }
    

    /** Permet à un visiteur de visiter cet opérateur de comparaison
     * @param v un visiteur pour cet opérateur de comparaison
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();	
	v.visiteInferieurStrict(this);
    }


}