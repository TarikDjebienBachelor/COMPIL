package ava.arbreAbstrait;

public abstract class Operateur extends ExpressionBinaire {

    public abstract void visite(Visiteur v) throws VisiteurException;

    /** Construit un opérateur étant donnés son opérande
     * gauche et son opérande droit.
     * @param g opérande gauche de l'expression
     * @param d opérande droit de l'expression
     * @throws IllegalArgumentException si <tt>g</tt> et/ou <tt>d</tt>
     * valent null.
     */
    public Operateur(Expression g, Expression d) {
	super(g, d);
    }


}
