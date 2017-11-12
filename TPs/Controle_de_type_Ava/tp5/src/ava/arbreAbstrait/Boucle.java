package ava.arbreAbstrait;

/** Bouche tant que d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class Boucle implements Instruction {

    // expression booléenne
    private Expression expr;

    // instructions
    private ListeInstruction corps;

    /** Construit une bouce while de condition booléenne
     * <tt>cond</tt> et de corps <tt>corps</tt>.
     * @param cond l'expression booléenne de cette conditionnelle
     * @param corps la liste d'instructions pour cette boucle while.
     * @throws IllegalArgumentException si <tt>cond</tt> vaut null.
     */
    public Boucle(Expression cond, ListeInstruction corps) {
	if (cond == null)
	    throw new IllegalArgumentException();
	this.expr = cond;
	this.corps = corps;
    }

    /** Retourne la condition booléenne de cette boucle.
     * @return l'expression booléenne de cette boucle
     */
    public Expression getCondition() {
	return this.expr;
    }

    /** Retourne la liste d'instructions de cette boucle.
     * @return le corps de cette boucle
     */
    public ListeInstruction getListeInstr() {
	return this.corps;
    }

    /** Permet la visite de cette boucle par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cette boucle.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException{
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteBoucle(this);

    }

}