package ava.testeurs;

import ava.analyseurs.*;

import java.io.*; 
import java_cup.runtime.Symbol;

/** Classe de test (fichiers de test positifs) pour l'analyseur
 * lexical Ava.
 * @author M. Nebut 08/07 revu 09/09
 */
public class TesteurPositifAnalyseurLexical {

    public static void main(String[] args) throws Exception {
	
	if (args.length != 1)
            System.out.println("Attention: un fichier est attendu.");
	new TesteurPositifAnalyseurLexical().run(args);

    }

    public void run(String[] args) throws Exception {
	Reader flotLecture = obtenirFlotDepuisFichierOuEntreeStandard(args);
	ScannerAva scanner = construireAnalyseurLexical(flotLecture);
	try {
	    avalerSymbolesFournisParScanner(scanner);
	    indiquerVerdictPositif();
	} catch (Exception e) {
	    indiquerVerdictNegatif(e);
	}
    }


    private Reader obtenirFlotDepuisFichierOuEntreeStandard 
	(String[] argsLigneCommande) 
	throws FileNotFoundException {

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
	System.out.println("Test positif OK");
    }

    private void indiquerVerdictNegatif(Exception e) {
	System.out.println("Test positif KO");
	e.printStackTrace();
    }
        
}