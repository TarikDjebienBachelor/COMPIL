package ava.executeurs;

import ava.analyseurs.*;

import ava.arbreAbstrait.Programme;
import ava.typeChecking.TypeChecker;
import ava.tableSymboles.TableSymboles;

import java.io.*; 
import java_cup.runtime.Symbol;

/** Lancement du contrôle de type, après construction de l'arbre abstrait
 * par l'analyseur syntaxique. Si un nom de fichier est fourni, ce
 * fichier est analysé, sinon c'est l'entrée standard qui l'est.
 * @author M. Nebut 08/07
 */
public class LanceurTypeChecker {


    public static void main(String[] args) throws Exception {
	if (args.length > 1)
            System.out.println("Attention: un seul fichier pris en compte");
	new LanceurTypeChecker().run(args);
    }

    public void run(String[] args) throws Exception {
	Reader flotLecture = obtenirFlotDepuisFichierOuEntreeStandard(args);
	ScannerAva scanner = construireAnalyseurLexical(flotLecture);
	ParserAva parser = new ParserAva(scanner);
	Symbol symboleProgramme = parser.parse();
	this.typeCheck(symboleProgramme);
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
	System.out.println("\n*********** table des symboles **************");
	System.out.println(tableSymboles.toString());
    }
}