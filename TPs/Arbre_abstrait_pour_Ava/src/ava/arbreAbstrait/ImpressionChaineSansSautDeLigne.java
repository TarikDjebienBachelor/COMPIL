package ava.arbreAbstrait;

/** Impression d'une chaîne dans un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class ImpressionChaineSansSautDeLigne implements ImpressionSansSautDeLigne {

    // chaîne à imprimer
    String chaine;

    /** Construit une impression de chaîne pour <tt>s</tt>.
     * @param s la chaîne à imprimer
     * @throws IllegalArgumentException si <tt>s</tt> vaut null.
     */
    public ImpressionChaineSansSautDeLigne(String s) throws IllegalArgumentException {
        if (s == null)
	    throw new IllegalArgumentException();
	this.chaine = s;
    }

    /** Retourne la chaîne à imprimer.
     * @return la chaîne à imprimer.
     */
    public String getChaine() {
	return this.chaine;
    }

    /** Permet à un visiteur de visiter cette impression.
     * @param v un visiteur pour cette impression
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteImpressionChaineSansSautDeLigne(this);
    }
    
}
