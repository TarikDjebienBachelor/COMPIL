package ava.tableSymboles;

/** Type énuméré pour les types de données existants en Ava. 
 * @author M. Nebut
 */
public class Type {
    /** Type non initialisé. */
    public static Type NOTYPE = new Type("NOTYPE");
    /** Type entier. */
    public static Type ENTIER = new Type("ENTIER");
    /** Type booléen. */
    public static Type BOOLEEN = new Type("BOOLEEN");
    /** Type programme */
    public static Type PROGRAM = new Type("PROGRAM");

    // version imprimable de ce type
    private String nom;

    // constructeur initialisant le nom du type
    private Type (String s) {
	this.nom = s;
    }

    /** Retourne une version imprimable de ce type. 
     * @return une version imprimable de ce type 
     */
    public String toString() {
	return this.nom;
    }
    
}