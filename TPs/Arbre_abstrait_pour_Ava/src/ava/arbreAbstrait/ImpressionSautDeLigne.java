package ava.arbreAbstrait;




/** Impression d'un retour à la ligne dans un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class ImpressionSautDeLigne implements ImpressionAvecSautDeLigne {

    /** Permet à un visiteur de visiter cette impression.
     * @param v un visiteur pour cette impression
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();	
	v.visiteImpressionSautDeLigne(this);
    }


}
