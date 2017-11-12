package ava.arbreAbstrait;

/** Conditionnelle abstraite d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public abstract class Conditionnelle implements Instruction {

    // expression booléenne
    private Expression expr;

    // partie then
    private ListeInstruction partieThen;

    /** Construit une conditionnelle de condition booléenne
     * <tt>cond</tt> et de partie alors <tt>alors</tt>.
     * @param cond l'expression booléenne de cette conditionnelle
     * @param alors la liste d'instructions pour la partie "alors" de
     * cette conditionnelle.
     * @throws IllegalArgumentException si <tt>cond</tt> ou
     * <tt>alors</tt> valent null.
     */
    public Conditionnelle(Expression cond, ListeInstruction alors) {
	if (cond == null || alors == null)
	    throw new IllegalArgumentException();
	this.expr = cond;
	this.partieThen = alors;
    }

    /** Retourne la condition booléenne de cette conditionnelle.
     * @return l'expression booléenne de cette conditionnelle
     */
    public Expression getCondition() {
	return this.expr;
    }

    /** Retourne la liste d'instructions pour la partie "alors" de
     * cette conditionnelle.
     * @return la partie "alors" de cette conditionnelle
     */
    public ListeInstruction getListeInstrAlors() {
	return this.partieThen;
    }

    

}