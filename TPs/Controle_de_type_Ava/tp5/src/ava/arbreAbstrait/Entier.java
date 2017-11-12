package ava.arbreAbstrait;


/** Entier d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class Entier implements Expression {

    // la valeur de cet entier (positive)
    private int valeur;

    /** Construit un entier de valeur la valeur de <tt>v</tt>.
     * @param v un Integer de valeur la valeur de cet entier, positive
     * @throws IllegalArgumentException si <tt>v</tt> vaut null ou sa valeur est strictement négative. 
     */
    public Entier(Integer v) {
	if (v == null)
	    throw new IllegalArgumentException();
	if (v.intValue() < 0)
	    throw new IllegalArgumentException();
	this.valeur = v.intValue();
    }


    /** Retourne la valeur de cet entier.
     * @return la valeur de cet entier.
     */
    public int getValeur() {
	return this.valeur;
    }

    /** Permet la visite de cet entier par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cet entier.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteEntier(this);
    }


}
