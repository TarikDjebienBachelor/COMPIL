package ava.tableSymboles;

import ava.tableSymboles.TableException;

/** Implantation de l'ensemble des attributs d'une variable. Contient
 * un type, et le futur index dans la table des variables du bytecode
 * généré.
 * @author M. Nebut 
 */
public class AttributsIdentificateur {
    // son index 
    private int index;
    // son type
    private Type type;
    // son nom
    private String nom;

    /** Construit un ensemble d'attribut contenant un nom passé en
     * paramètre, un type valant Type.NOTYPE, un index valant -1
     * @param n l'attribut nom contenu par cet ensemble d'attributs 
     */
    public AttributsIdentificateur(String n) {
	this.nom = n;
	this.index = -1;
	this.type = Type.NOTYPE;
    }

    /** Fixe l'index contenu par cet ensemble d'attributs à la valeur passée en
     * paramètre.
     * @param i la valeur d'index contenue par cet ensemble d'attributs
     */
    public void setIndex(int i) {
	this.index = i;
    }

    /** Retourne l'index contenu par cet ensemble d'attributs.
     * @return l'index contenu par cet ensemble d'attributs.
     */
    public Integer getIndex() {
	return new Integer(this.index);
    }



    /** Fixe le type contenu par cet ensemble d'attributs à la valeur passée en
     * paramètre.
     * @param t la valeur du type contenu par cet ensemble d'attributs
     */
    public void setType(Type t) {
	this.type = t;
    }

    /** Retourne le type contenu par  cet ensemble d'attributs.
     * @return le type contenu par  cet ensemble d'attributs.
     */
    public Type getType() {
	return this.type;
    }

    /** Fixe le nom contenu par cet ensemble d'attributs à la valeur passée en
     * paramètre.
     * @param n le nom contenu par cet ensemble d'attributs
     */
    public void setNom(String n) {
	this.nom = n;
    }


    /** Retourne le nom contenu par cet ensemble d'attributs.
     * @return le nom contenu par cet ensemble d'attributs.
     */
    public String getNom() {
	return this.nom;
    }
    
    /** Retourne une version imprimable de cet ensemble d'attributs.
     * @return une version String de cet ensemble d'attributs.
     */
    public String toString() {
	String res = "index = " + this.index + "\t" + "type = " + this.type.toString();
	return res;
    } 
   
}
