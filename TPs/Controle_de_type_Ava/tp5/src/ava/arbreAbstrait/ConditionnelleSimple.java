package ava.arbreAbstrait;

/** Conditionnelle d'un programme Ava : conditionnelle sans partie else.
 * @author M. Nebut
 * @version 11/07
 */
public class ConditionnelleSimple extends Conditionnelle {


    /** Construit une conditionnelle de condition booléenne
     * <tt>cond</tt> et de partie alors <tt>alors</tt>.
     * @param cond l'expression booléenne de cette conditionnelle
     * @param alors la liste d'instructions pour la partie "alors" de
     * cette conditionnelle.
     * @throws IllegalArgumentException si <tt>cond</tt> ou
     * <tt>alors</tt> valent null.
     */
    public ConditionnelleSimple(Expression cond, ListeInstruction alors) {
	super(cond,alors);
    }

    /** Permet la visite de cette conditionnelle par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cette conditionnelle.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteConditionnelleSimple(this);
    }

    

}