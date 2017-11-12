package ava.executeurs;

import ava.analyseurs.*;
import ava.arbreAbstrait.Programme;
import ava.typeChecking.TypeChecker;
import ava.tableSymboles.TableSymboles;
import ava.genCode.*;

import java.io.*; 
import java_cup.runtime.Symbol;

/** Lancement de l'analyseur syntaxique Ava + contrôle de type + génération de code.
 * @author M. Nebut 08/07
 */
public class LanceurGenerateurCode {

    public static void main(String[] args) throws Exception {
	if (args.length > 1)
            System.out.println("Attention: un seul fichier pris en compte");
	new LanceurGenerateurCode().run(args);
    }

    public void run(String[] args) throws Exception {
	Reader flotLecture = obtenirFlotDepuisFichierOuEntreeStandard(args);
	ScannerAva scanner = construireAnalyseurLexical(flotLecture);
	ParserAva parser = new ParserAva(scanner);
	Symbol symboleProgramme = parser.parse();
	Programme programme = (Programme) symboleProgramme.value;
	this.typeCheck(programme);
	this.genererCode(programme);
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

    private void typeCheck(Programme programme) throws Exception {
	TableSymboles tableSymboles = new TableSymboles();
	TypeChecker typeChecker = new TypeChecker(tableSymboles,programme);
	typeChecker.typeCheck();
    }

    private void genererCode(Programme programme) throws Exception {
	GenByteCode genCode = new GenByteCode(programme.getNom());
	GenerateurCode generateur = new GenerateurCode(genCode,programme);
	generateur.generationCode();
	this.afficherTables(genCode);
    }

    private void afficherTables(GenByteCode genCode) throws Exception {

	/** Table constantes. */
	System.out.println("\nTable des constantes *********\n");	
	System.out.println(genCode.dumpConstantsTable());
		
	/** Table variables. */
	System.out.println("Table des variables *********\n");
	System.out.println(genCode.dumpVariablesTable());
	
	/** Code. */
	System.out.println("Code *********\n");
	System.out.println(genCode.dumpCode());
    }

}