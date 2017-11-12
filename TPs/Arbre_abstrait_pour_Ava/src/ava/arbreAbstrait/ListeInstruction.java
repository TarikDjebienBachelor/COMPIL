package ava.arbreAbstrait;

import java.util.LinkedList;
import java.util.Iterator;



/** Liste des instructions d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public class ListeInstruction implements Visitable {

    /** La liste des instructions. */
    private LinkedList liste;

    /** Construit une liste d'instructions vide. */
    public ListeInstruction() {
	this.liste = new LinkedList();
    }

    /** Construit une liste d'instructions  contenant l'instruction
     * <tt>i</tt>.
     * @param i l'instruction contenue dans la liste d'instructions  à
     * créer.     
     * @throws IllegalArgumentException si i vaut null
     */ 
    public ListeInstruction(Instruction i) {
	this();
	if (i == null) 
	    throw new IllegalArgumentException();	
	this.liste.add(i);
    }

    /** Ajoute en tête de cette liste d'instructions l'instruction
     * <tt>i</tt>.
     * @param i l'instruction à ajouter en tête de cette liste d'instructions.
     * @throws IllegalArgumentException si i vaut null
     */
    public void ajoutInstrEnTete(Instruction i) {
	if (i == null) 
	    throw new IllegalArgumentException();
	this.liste.addFirst(i);
    }
    
    /** Ajoute en queue de cette liste d'instructions l'instruction
     * <tt>i</tt>.
     * @param i l'instruction à ajouter en queue de cette liste d'instructions.
     * @throws IllegalArgumentException si d vaut null
     */
    public void ajoutInstrEnQueue(Instruction i) {
	if (i == null) 
	    throw new IllegalArgumentException();
	this.liste.addLast(i);
    }

    /** Concatène à cette liste d'intructions la liste d'instructions
     *  <tt>li</tt>.
     * @param li la liste d'instructions à concaténer à cette liste
     * d'instructions.
     * @throws IllegalArgumentException si li vaut null
     */
    public void concat(ListeInstruction li) {
	if (li == null) 
	    throw new IllegalArgumentException();
	this.liste.addAll(li.liste);
    }

    /** Retourne un itérateur sur cette liste d'instructions.
     * @return un itérateur sur cette liste d'instructions.
     */
    public Iterator iterator() {
	return this.liste.iterator();
    }

    /** Permet à un visiteur de visiter cette liste d'instructions.
     * @param v un visiteur pour cette liste d'instructions
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();	
	v.visiteListeInstr(this);
    }




}
