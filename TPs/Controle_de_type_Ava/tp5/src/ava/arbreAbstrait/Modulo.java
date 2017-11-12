package ava.arbreAbstrait;

/** Modulo sur entiers.
 * @author M. Nebut
 * @version 11/07
 */
public class Modulo extends Operateur {

    /** Construit une expression �tant donn�s son op�rande
     * gauche et son op�rande droit.
     * @param g op�rande gauche de l'expression
     * @param d op�rande droit de l'expression
     * @throws IllegalArgumentException si <tt>g</tt> et/ou <tt>d</tt>
     * valent null.
     */
    public Modulo(Expression g, Expression d) {
	super(g,d);
    }

    /** Permet la visite de cette modulo par un visiteur pass� en
     * param�tre.
     * @param v le visiteur pour cette modulo.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteModulo(this);
    }


}