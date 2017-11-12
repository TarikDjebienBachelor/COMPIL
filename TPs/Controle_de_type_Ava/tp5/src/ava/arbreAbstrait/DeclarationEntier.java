package ava.arbreAbstrait;

/** Déclaration de variable de type entier d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class DeclarationEntier extends Declaration {

    /** Construit une déclarations pour l'identificateur <tt>id</tt>.
     * @param id l'identificateur déclaré
     * @throws IllegalArgumentException si <tt>id</tt> vaut null.
     */
    public DeclarationEntier(String id) {
	super(id);
    }

    /** Permet à un visiteur de visiter cette déclaration de type entier.
     * @param v un visiteur pour cette déclaration
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();	
	v.visiteDeclInt(this);
    }


}