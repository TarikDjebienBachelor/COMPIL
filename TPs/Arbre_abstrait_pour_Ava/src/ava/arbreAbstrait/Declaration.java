package ava.arbreAbstrait;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/** Déclaration de variable d'un programme Ava.
 * @author M. Nebut
 * @version 11/07
 */
public abstract class Declaration implements Visitable {

    /** Nom de l'identificateur déclaré. */
    private String ident;

    /** Construit une déclarations pour l'identificateur <tt>id</tt>.
     * @param id l'identificateur déclaré
     * @throws IllegalArgumentException si <tt>id</tt> vaut null.
     */
    public Declaration(String id) {
	if (id == null)
	    throw new IllegalArgumentException();
	this.ident = id;
    }

    /** Retourne l'identificateur déclaré. 
     * @return l'identificateur de cette déclaration.
     */
    public String getIdent() {
	return this.ident;
    }

}