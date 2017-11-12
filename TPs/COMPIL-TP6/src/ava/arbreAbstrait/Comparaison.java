package ava.arbreAbstrait;


/** Comparaison dans un programme
 * Ava.
 * @author M. Nebut
 * @version 11/07
 */
public abstract class Comparaison extends  ExpressionBinaire {


    /** Construit une comparaison étant donnés son opérande
     * gauche et son opérande droit.
     * @param g opérande gauche de la comparaison
     * @param d opérande droit de la comparaison
     * @throws IllegalArgumentException si <tt>g</tt> et/ou <tt>d</tt>
     * valent null.
     */
    public Comparaison(Expression g, Expression d) {
	super(g,d);
    }


}