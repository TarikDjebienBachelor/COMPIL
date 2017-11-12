package ava.tableSymboles;
import java.util.*;

/** Implantation d'une table des symboles.
 * @author M. Nebut
 */
public class TableSymboles {
    // la table de hash
    private Map table;
    // gargl : accès par index et non par nom, pour éviter des recherches
    private List liste;

    /** construit une table des symboles vide. */
    public TableSymboles() {
	this.table = new HashMap();
	this.liste = new ArrayList();
    }

    /** Ajoute une variable dont le nom est passé en paramètre dans la
     * table (sans effet si la variable y était déjà), fixe son index
     * dans la table, retourne son ensemble d'attributs.
     * @param n le nom de la variable à ajouter
     * @return les attributs de cette variable
     */
    public AttributsIdentificateur ajoutIdentificateur(String n) {
	if (! this.table.containsKey(n)) {
	    AttributsIdentificateur att = new AttributsIdentificateur(n);
	    att.setIndex(this.table.size());
	    this.table.put(n,att);
	    this.liste.add(att);
	    return att;
	} else { 
	    return (AttributsIdentificateur) this.table.get(n);
	}
    }

    /** Retourne vrai si la table contient la variable dont le nom est
     * passé en paramètre. 
     * @param n le nom de la variable
     * @return vrai si la variable de nom n est dans la table
     */
    public boolean contientIdentificateur(String n) {
	return this.table.containsKey(n);
    }

    /** Retourne l'ensemble des attributs de la variable dont le nom est passé en
     * paramètre, null si elle n'appartient pas à la table.
     * @param n le nom de la variable dont on veut les attributs
     */
    public AttributsIdentificateur getAttributsIdentificateur(String n) {
	return (AttributsIdentificateur) this.table.get(n);
    }

    /** Retourne l'ensemble des attributs de la variable dont l'index
     * est passé en paramètre, null si elle n'appartient pas à la
     * table.
     * @param i l'index de la variable dont on veut les attributs
     */
    public AttributsIdentificateur getAttributsIdentificateur(Integer i) {
	return (AttributsIdentificateur) this.liste.get(i.intValue());
    }

    /** Retourne une version imprimable de cette table. 
     * @return une version String de cette table.
     */
    public String toString() {
	Iterator it = this.table.keySet().iterator();
	String res = "";
	while (it.hasNext()) {
	    String nomVar = (String) it.next();
	    res += nomVar + "\t" + " : ";
	    res += ((AttributsIdentificateur) this.table.get(nomVar)).toString() + "\n";
	}
	return res;
    }
} 