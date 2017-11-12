package ava.arbreAbstrait;


/** Lecture d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class Lecture implements Instruction {

    // variable lue
    private String ident;

    /** Construit une lecture pour <tt>nom</tt>
     * @param nom l'identificateur affecté
     * @throws IllegalArgumentException si <tt>nom</tt> vaut null.
     */
    public Lecture(String nom) {
	if (nom == null)
	    throw new IllegalArgumentException();
	this.ident = nom;
    }

    /** Retourne la variable affectée de cette lecture.
     * @return le nom de la variable affectée
     */
    public String getIdent() { 
	return this.ident;
    }


    /** Permet la visite de cette lecture par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour cette lecture.
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();
	v.visiteLecture(this);
    }


}
