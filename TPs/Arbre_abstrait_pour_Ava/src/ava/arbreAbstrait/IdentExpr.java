package ava.arbreAbstrait;


/** Identificateur dans une expression Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class IdentExpr implements Expression {

    // le nom de cet identificateur
    private String nom;

    /** Construit un identificateur de nom <tt>nom</tt>.
     * @param nom le nom de cet identificateur
     * @throws IllegalArgumentException si <tt>nom</tt> vaut null
     */
    public IdentExpr(String nom) {
	if (nom == null)
	    throw new IllegalArgumentException();
	this.nom = nom;
    }


    /** Retourne le nom de cet identificateur.
     * @return le nom de cet identificateur..
     */
    public String getNom() {
	return this.nom;
    }

    /** Permet la visite de cet identificateur par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cet identificateur.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteIdentExpr(this);
    }


}
