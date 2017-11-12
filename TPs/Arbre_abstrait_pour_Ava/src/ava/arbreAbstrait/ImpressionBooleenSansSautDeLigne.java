package ava.arbreAbstrait;




/** Impression d'un booléen dans un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class ImpressionBooleenSansSautDeLigne implements ImpressionSansSautDeLigne {

    // expression à imprimer
    private Expression expr;


    /** Construit une impression d'expression booléenne pour <tt>e</tt>.
     * @param e l'expression booléenne à imprimer
     * @throws IllegalArgumentException si <tt>e</tt> vaut null.
     */
    public ImpressionBooleenSansSautDeLigne(Expression e) {
	if (e == null)
	    throw new IllegalArgumentException();
	this.expr = e;
    }

    /** Retourne l'impression à imprimer.
     * @return l'impression à imprimer.
     */
    public Expression getExpr() {
	return this.expr;
    }

    /** Permet à un visiteur de visiter cette impression.
     * @param v un visiteur pour cette impression
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();	
	v.visiteImpressionBooleenSansSautDeLigne(this);
    }
    
}
