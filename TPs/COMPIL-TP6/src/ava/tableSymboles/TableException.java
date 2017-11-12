package ava.tableSymboles;

/** Exceptions générées lors d'une erreur de manipulation de table.
 * @author M. Nebut.
 */
public class TableException extends Exception {
    public TableException() {
	super("Erreur de manip de table");
    }

    public TableException(String msg) {
	super("Erreur de manip de table" + " : " + msg);
    }
    
}