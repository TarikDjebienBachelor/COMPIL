package ava.testeurs;

import ava.analyseurs.*;
import ava.typeChecking.*;

import ava.tableSymboles.TableSymboles;
import ava.arbreAbstrait.Programme;

import java.io.*; // pour gestion des fichiers
import java_cup.runtime.Symbol;

/** Classe de test (test négatifs) pour 
 * le contrôle de type Ava. 
 * @author M. Nebut 08/07
 */
public class TesteurNegatifTypeChecker {
	
    public static void main(String[] args) throws Exception {	
	if (args.length != 1)
            System.out.println("Attention: un fichier est attendu.");
	new TesteurNegatifTypeChecker().run(args);
    }
	    
    public void run(String[] args) throws Exception {
	Reader flotLecture = obtenirFlotDepuisFichierOuEntreeStandard(args);
	ScannerAva scanner = construireAnalyseurLexical(flotLecture);
	ParserAva parser = new ParserAva(scanner);
	Symbol symboleProgramme = parser.parse();
	try {
	    this.typeCheck(symboleProgramme);
	    indiquerVerdictNegatif();
	} catch (TypeCheckingException e) {
	    indiquerVerdictPositif();
	}
    }

    private Reader obtenirFlotDepuisFichierOuEntreeStandard (String[] argsLigneCommande) 
	throws FileNotFoundException {

	if (argsLigneCommande.length == 0)
	    return new InputStreamReader(System.in);
	return new FileReader(argsLigneCommande[0]);
    }

    private ScannerAva construireAnalyseurLexical(Reader flot) {
	return new ScannerAva(flot);		
    }    

    private void typeCheck(Symbol symboleProgramme) throws Exception {
	Programme p = (Programme) symboleProgramme.value;
	TableSymboles tableSymboles = new TableSymboles();
	TypeChecker typeChecker = new TypeChecker(tableSymboles,p);
	typeChecker.typeCheck();
    }


    private void indiquerVerdictPositif() {
	System.out.println("Test negatif OK");
    }

    private void indiquerVerdictNegatif() {
	System.out.println("Test negatif KO, flot d'entrée accepté");
    }

}