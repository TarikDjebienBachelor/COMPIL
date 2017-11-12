package ava.arbreAbstrait;

/** Interface qui définit pour un nœud de l'arbre abstrait la capacité
 * à être visitée.
 * @author M. Nebut
 * @version 11/07
 */
public interface Visitable {
    
    /** Permet la visite d'un nœud de l'arbre par un visiteur passé en
     * paramètre.
     * @param v le visiteur pour ce nœud.
     */
    public void visite(Visiteur v) throws VisiteurException; 

 }