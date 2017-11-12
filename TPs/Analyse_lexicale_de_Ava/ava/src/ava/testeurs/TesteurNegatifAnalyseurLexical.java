package ava.testeurs;

import ava.analyseurs.*;

import java.io.*; 
import java_cup.runtime.Symbol;

/** Classe de test (fichiers de test negatif) pour l'analyseur
 * lexical Ava.
 * @author M. Nebut 08/07 revu 09/09
 */
public class TesteurNegatifAnalyseurLexical {

    public static void main(String[] args) throws Exception {
	
	if (args.length != 1)
            System.out.println("Attention: un fichier est attendu.");
	new TesteurNegatifAnalyseurLexical().run(args);

    }

    public void run(String[] args) throws Exception {
	Reader flotLecture = obtenirFlotDepuisFichierOuEntreeStandard(args);
	ScannerAva scanner = construireAnalyseurLexical(flotLecture);
	try {
	    avalerSymbolesFournisParScanner(scanner);
	    indiquerVerdictNegatif();
	} catch (ScannerException e) {
	    indiquerVerdictPositif();
	}
    }


    private Reader obtenirFlotDepuisFichierOuEntreeStandard 
	(String[] argsLigneCommande) throws FileNotFoundException {
	return new FileReader(argsLigneCommande[0]);
    }

    private ScannerAva construireAnalyseurLexical(Reader flot) {
	return new ScannerAva(flot);		
    }    

    private void avalerSymbolesFournisParScanner(ScannerAva scanner) throws Exception {
	boolean finAnalyse = false;
	Symbol symboleCourant = null;
	do {
	    symboleCourant = obtenirProchainSymbole(scanner);
	    finAnalyse = (symboleCourant.sym == TypeSymboles.EOF);
	} while (! finAnalyse);
    }

    private Symbol obtenirProchainSymbole(ScannerAva scanner) throws Exception {
	return scanner.next_token();
    }

    private void indiquerVerdictPositif() {
	System.out.println("Test negatif OK");
    }

    private void indiquerVerdictNegatif() {
	System.out.println("Test negatif KO, flot d'entrée accepté");
    }
        
}