package ava.arbreAbstrait;

/** Affectation d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class Affectation implements Instruction {

    // variable membre gauche de l'affectation
    private String ident;

    // membre droit de l'affectation
    private Expression expr;

    /** Construit une affectation de membre gauche <tt>nom</tt> et de
     * membre droit <tt>e</tt>.
     * @param nom l'identificateur affecté
     * @param e l'expression affectée à <tt>nom</tt>
     * @throws IllegalArgumentException si <tt>nom</tt> et/ou
     * <tt>e</tt> valent null.
     */
    public Affectation(String nom, Expression e) {
	if (nom == null || e == null)
	    throw new IllegalArgumentException();
	this.ident = nom;
	this.expr = e;
    }

    /** Retourne le membre gauche de cette affectation.
     * @return le nom de la variable affectée
     */
    public String getIdent() { 
	return this.ident;
    }

    /** Retourne le membre droit de cette affectation.
     * @return l'expression affectée au membre gauche de l'affectation
     */
    public Expression getExpr() {
	return this.expr;
    }

    /** Permet la visite de cette affectation par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cette affectation.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteAffect(this);
    }


}