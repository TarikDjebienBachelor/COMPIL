package ava.arbreAbstrait;

/** Consitionnelle d'un programme Ava : conditionnelle avec partie else.
 * @author M. Nebut
 * @version 11/07
 */
public class ConditionnelleAvecAlternative extends Conditionnelle {

    // partie sinon
    private ListeInstruction partieElse;

    /** Construit une conditionnelle de condition booléenne
     * <tt>cond</tt>, de partie alors <tt>alors</tt> et de partie
     * sinon <tt>sinon</tt>.
     * @param cond l'expression booléenne de cette conditionnelle
     * @param alors la liste d'instructions pour la partie "alors" de
     * cette conditionnelle.
     * @param sinon la liste d'instructions pour la partie "sinon" de
     * cette conditionnelle.
     * @throws IllegalArgumentException si <tt>cond</tt> ou
     * <tt>alors</tt> ou <tt>sinon</tt> valent null.
     */
    public ConditionnelleAvecAlternative(Expression cond, ListeInstruction alors, ListeInstruction sinon) {
	super(cond,alors);
	if (sinon == null)
	    throw new IllegalArgumentException();
	this.partieElse = sinon;
    }


    /** Retourne la liste d'instructions pour la partie "sinon" de
     * cette conditionnelle.
     * @return la partie "sinon" de cette conditionnelle
     */
    public ListeInstruction getListeInstrSinon() {
	return this.partieElse;
    }


    /** Permet la visite de cette conditionnelle par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cette conditionnelle.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteConditionnelleAvecAlternative(this);
    }

    

}