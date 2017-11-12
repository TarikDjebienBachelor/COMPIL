package ava.arbreAbstrait;



/** Un programme Ava, représenté par son nom, la liste de ses
 * déclarations et la liste de ses instructions.
 * @author M. Nebut
 * @version 11/07
 */
public class Programme implements Visitable {

    /** Nom du programme. */
    private String nom;

    /** Liste des déclarations de ce programme. */
    private ListeDeclaration listeDecl;

    /** Liste des instructions de ce programme. */
    private ListeInstruction listeInstr;

    /** Construit un programme étant donné son nom, la liste de ses
     * déclarations et la liste de ses instructions.
     * @param nom le nom de ce programme
     * @param ld la liste des declarations de ce programme
     * @param li la liste des instructions de ce programme
     * @throws IllegalArgumentException si <tt>nom</tt> vaut null.	  
     */
    public Programme (String nom, ListeDeclaration ld, ListeInstruction li) {
	if (nom == null || ld == null || li == null)
	    throw new IllegalArgumentException();
	this.nom = nom;
	this.listeDecl = ld;
	this.listeInstr = li;
    }

    /** Retourne le nom de ce programme.
     * @return le nom de ce programme
     */
    public String getNom() {
	return this.nom;
    }

    /** Retourne la liste des déclarations de ce programme.
     * @return la liste des déclarations de ce programme
     */
    public ListeDeclaration getListeDecl() {
	return this.listeDecl;
    }

    /** Retourne la liste des instructions de ce programme.
     * @return la liste des instructions de ce programme
     */
    public ListeInstruction getListeInstr() {
	return this.listeInstr;
    }

    /** Permet à un visiteur de visiter ce programme.
     * @param v un visiteur pour ce programme
     * @throws VisiteurException en cas d'erreur pendant la visite
     * @throws IllegalArgumentException si <tt>v</tt> vaut null
     */
    public void visite(Visiteur v) throws VisiteurException {
	if (v == null)
	    throw new IllegalArgumentException();	
	v.visiteProgramme(this);
    }







}
