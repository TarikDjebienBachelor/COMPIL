package ava.arbreAbstrait;

import java.util.LinkedList;
import java.util.Iterator;

/** Liste des déclarations d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class ListeDeclaration implements Visitable {

    /** La liste des déclarations. */
    private LinkedList liste;

    /** Construit une liste de déclarations vide. */
    public ListeDeclaration() {
	this.liste = new LinkedList();
    }

    /** Construit une liste de déclarations contenant la déclaration
     * <tt>d</tt>.
     * @param d la déclaration contenue dans la liste de déclarations à
     * créer.     
     * @throws IllegalArgumentException si d vaut null
     */ 
    public ListeDeclaration(Declaration d) {
	this();
	if (d == null) 
	    throw new IllegalArgumentException();	
	this.liste.add(d);
    }

    /** Ajoute en tête de cette liste de déclaration la déclaration
     * <tt>d</tt>.
     * @param d la déclaration à ajouter en tête de cette liste de
     * déclarations.
     * @throws IllegalArgumentException si d vaut null
     */
    public void ajoutDeclEnTete(Declaration d) {
	if (d == null) 
	    throw new IllegalArgumentException();
	this.liste.addFirst(d);
    }
    
    /** Ajoute en queue de cette liste de déclaration la déclaration
     * <tt>d</tt>.
     * @param d la déclaration à ajouter en queue de cette liste de
     * déclarations.
     * @throws IllegalArgumentException si d vaut null
     */
    public void ajoutDeclEnQueue(Declaration d) {
	if (d == null) 
	    throw new IllegalArgumentException();
	this.liste.addLast(d);
    }

    /** Concatène à cette liste de déclaration la liste de
     * déclarations <tt>ld</tt>.
     * @param ld la liste de déclarations à concaténer à cette liste
     * de déclarations.
     * @throws IllegalArgumentException si ld vaut null
     */
    public void concat(ListeDeclaration ld) {
	if (ld == null) 
	    throw new IllegalArgumentException();
	this.liste.addAll(ld.liste);
    }

    /** Retourne un itérateur sur cette liste de déclarations.
     * @return un itérateur sur cette liste de déclarations.
     */
    public Iterator iterator() {
	return this.liste.iterator();
    }

    /** Permet à un visiteur de visiter cette liste de déclarations.
     * @param v un visiteur pour cette liste de déclarations
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();	
	v.visiteListeDecl(this);
    }



}