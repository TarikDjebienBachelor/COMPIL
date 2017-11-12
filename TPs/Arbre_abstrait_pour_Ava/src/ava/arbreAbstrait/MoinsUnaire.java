package ava.arbreAbstrait;

/** Moins unaire sur entier.
 * @author M. Nebut
 * @version 11/07
 */
public class MoinsUnaire extends ExpressionUnaire {

    /** Construit une prise d'opposé étant donné son opérande
     * @param e opérande dont on veut l'opposé 
     * @throws IllegalArgumentException si <tt>e</tt> vaut null.
     */
    public MoinsUnaire(Expression e) {
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
	v.visiteMoinsUnaire(this);
    }

}